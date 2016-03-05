package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An IChatComponent that converts a format to a set of IChatComponents
 *
 * Each of the parenthesis pairs represent an IChatComponent with its
 * own ChatStyle (color, bold, underlined, etc.)
 *
 * Example: {2|Entity number }{%s}{2| is the }{7l| %s}
 * This format will create the following IChatComponents:
 *  - "Entity number "; with DARK_GREEN color
 *  - %s; one of the parameters sent by the caller (as IChatComponent or
 *                      IChatFormat, since it's missing the "|" style delimiter character
 *  - " is the "; with DARK_GREEN color
 *  - %s; one of the parameters sent by the caller (as String since it HAS "|" style delimiter character)
 */
public class ChatComponentFormatted extends ChatComponentText {
    // Regular Expression to match variables in text
    // The first group is text that shows up before the variable. If no text is present it is either null or an empty string
    // Second group contains ONLY the variable (%s)
    private static Pattern textPattern = Pattern.compile("(.*?)(%s)");

    // Regular expression to handle cases where a variable (%s) is not the last
    // TODO Find a way to integrate this with the main pattern
    private static Pattern textPattern2 = Pattern.compile("(.+)(%s)(.+)");

    public ChatComponentFormatted(String format, Object... args) {
        super("");

        String[] components = StringUtils.split(format, "{}");
        int argNumber = 0;

        for (String component : components) {
            String[] parts = component.split("\\|");
            ChatStyle chatStyle = null;
            String actualText;

            // Get text and chat style
            if (parts.length == 2) {
                chatStyle = getStyle(parts[0]);
                actualText = parts[1];
            } else if (parts.length == 1) {
                actualText = parts[0];
            } else {
                throw new FormatException("Format " + component + " is not valid. Valid format: {modifiers|text}");
            }

            // Match the text for variables (%s)
            Matcher m = textPattern.matcher(actualText);

            // Loop over the matches
            while(m.find()) {
                String group1 = m.group(1);
                // Group1 should always be plain text (no variable) so just add as such (unless null or empty string)
                if (group1 != null && !"".equals(group1.trim())) {
                    this.appendSibling(new ChatComponentText(group1));
                }

                String group2 = m.group(2);
                // This check is probably redundant, but just in case
                if (group2 != null && !"".equals(group2.trim())) {
                    if (args[argNumber] instanceof  IChatFormat) {
                        IChatComponent formattedArgument = ((IChatFormat) args[argNumber++]).toChatMessage();
                        this.appendSibling(formattedArgument);
                    } else if (args[argNumber] instanceof IChatComponent) {
                        this.appendSibling((IChatComponent) args[argNumber++]);
                    } else if (args[argNumber] instanceof String) {
                        this.appendSibling(new ChatComponentText((String) args[argNumber++]));
                    } else if (args[argNumber] instanceof Number) {
                        this.appendSibling(new ChatComponentText(((Number) args[argNumber++]).toString()));
                    } else {
                        throw new FormatException("Argument at position " + argNumber + " (" + args[argNumber].getClass().getName() + ") does not implement IChatFormat interface");
                    }
                }
            }

            // Match text at end of string if a variable (%s) is not last
            Matcher m2 = textPattern2.matcher(actualText);
            if (m2.find()) {
                this.appendSibling(new ChatComponentText(m2.group(3)));
            }

            // Set chat style
            this.setChatStyle(chatStyle);
        }
    }

    /**
     * Converts the modifiers String to a ChatStyle
     * [modifiers|  some text]
     *  ^^^^^^^^    ^^^^^^^^^
     *  STYLE  for  THIS TEXT
     */
    private ChatStyle getStyle(String modifiers) {

        ChatStyle chatStyle = new ChatStyle();

        for (char c : modifiers.toCharArray()) {
            applyModifier(chatStyle, c);
        }

        return chatStyle;
    }

    /**
     * Applies modifier to the style
     * Returns whether or not the modifier was valid
     */
    private boolean applyModifier(ChatStyle chatStyle, char modifier) {
        if (modifier >= '0' && modifier <= '9' || modifier >= 'a' && modifier <= 'f') {
            chatStyle.setColor(ColorUtils.colorMap.get(modifier));
            return true;
        }
        switch (modifier) {
            case 'k': chatStyle.setObfuscated(true); return true;
            case 'l': chatStyle.setBold(true); return true;
            case 'm': chatStyle.setStrikethrough(true); return true;
            case 'n': chatStyle.setUnderlined(true); return true;
            case 'o': chatStyle.setItalic(true); return true;
        }
        return false;
    }

    @Override
    public String getUnformattedTextForChat() {
        return "";
    }

    @Override
    public ChatComponentText createCopy() {
        return null;
    }
}
