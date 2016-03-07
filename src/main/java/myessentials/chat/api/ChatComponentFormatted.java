package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
        this(format, Arrays.asList(args).iterator());
    }

    public ChatComponentFormatted(String format, Iterator args) {
        String[] components = StringUtils.split(format, "{}");
        IChatComponent buffer = new ChatComponentList();

        for (String component : components) {
            String[] parts = component.split("\\|", 2);

            if(parts.length >= 2) {
                ChatStyle chatStyle = getStyle(parts[0]);

                if (parts[0].contains("N") && buffer.getSiblings().size() > 0) {
                    // Reset the buffer if the buffer reset modifier is found
                    this.appendSibling(buffer);
                    buffer = new ChatComponentList();
                }

                String[] textWithHover = parts[1].split(" << ");
                String actualText = textWithHover[0];
                while (actualText.contains("%s")) {
                    actualText = actualText.replaceFirst("%s", args.next().toString());
                }
                IChatComponent message = new ChatComponentText(actualText).setChatStyle(chatStyle);

                if (textWithHover.length == 2) {
                    chatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentFormatted("{" + textWithHover[1] + "}", args)));
                }

                buffer.appendSibling(message);

            } else if (parts.length == 1 && parts[0].equals("%s")) {

                // TODO: Instead of %s use other identifiers for lists of elements or container (maybe)
                Object currArg = args.next();
                if (currArg instanceof IChatFormat) {
                    IChatComponent formattedArgument = ((IChatFormat) currArg).toChatMessage();
                    buffer.appendSibling(formattedArgument);
                } else if (currArg instanceof IChatComponent) {
                    buffer.appendSibling((IChatComponent) currArg);
                } else if (currArg instanceof ChatComponentContainer) {

                    this.appendSibling(buffer);
                    buffer = new ChatComponentList();
                    for (IChatComponent message : (ChatComponentContainer) currArg) {
                        this.appendSibling(message);
                    }
                } else {
                    throw new FormatException("An argument in format " + format + " does not implement IChatFormat interface");
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

    /**
     * Adds a ChatComponentText between all of the siblings
     * This can be used for easily displaying a onHoverText on multiple lines
     */
    public ChatComponentFormatted applyDelimiter(String delimiter) {
        List<IChatComponent> newSiblings = new ArrayList<IChatComponent>();
        for (IChatComponent component : (List<IChatComponent>) siblings) {
            if (newSiblings.size() > 0) {
                newSiblings.add(new ChatComponentText(delimiter));
            }
            newSiblings.add(component);
        }
        this.siblings = newSiblings;
        return this;
    }
}
