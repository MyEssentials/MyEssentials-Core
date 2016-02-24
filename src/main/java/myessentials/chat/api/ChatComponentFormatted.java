package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang.StringUtils;

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
public class ChatComponentFormatted extends ChatComponentList {

    public ChatComponentFormatted(String format, Object... args) {
        String[] components = StringUtils.split(format, "{}");
        int argNumber = 0;
        IChatComponent buffer = new ChatComponentList();

        for (String component : components) {
            String[] parts = component.split("\\|");

            if(parts.length == 2) {
                ChatStyle chatStyle = getStyle(parts[0]);

                if (parts[0].contains("N") && buffer.getSiblings().size() > 0) {
                    // Reset the buffer if the buffer reset modifier is found
                    this.appendSibling(buffer);
                    buffer = new ChatComponentList();
                }

                String actualText = parts[1];
                while (actualText.contains("%s")) {
                    actualText = actualText.replaceFirst("%s", args[argNumber++].toString());
                }
                buffer.appendSibling(new ChatComponentText(actualText).setChatStyle(chatStyle));

            } else if (parts.length == 1 && parts[0].equals("%s")) {

                // TODO: Instead of %s use other identifiers for lists of elements or container (maybe)
                if (args[argNumber] instanceof IChatFormat) {
                    IChatComponent formattedArgument = ((IChatFormat) args[argNumber++]).toChatMessage();
                    buffer.appendSibling(formattedArgument);
                } else if (args[argNumber] instanceof IChatComponent) {
                    buffer.appendSibling((IChatComponent) args[argNumber++]);
                } else if (args[argNumber] instanceof ChatComponentContainer) {

                    this.appendSibling(buffer);
                    buffer = new ChatComponentList();
                    for (IChatComponent message : (ChatComponentContainer) args[argNumber]) {
                        this.appendSibling(message);
                    }
                    argNumber++;
                } else {
                    throw new FormatException("Argument at position " + argNumber + " in format " + format + " does not implement IChatFormat interface");
                }

            } else {
                throw new FormatException("Format " + format + " is not valid. Valid format: {modifiers|text}");
            }
        }
        this.appendSibling(buffer);
    }

    /**
     * Converts the modifiers String to a ChatStyle
     * {modifiers|  some text}
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
}
