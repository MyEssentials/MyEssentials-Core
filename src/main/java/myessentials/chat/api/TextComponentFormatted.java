package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import net.minecraft.event.HoverEvent;
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
 * Example: {2|Entity number }{%s}{2| is the }{7l| %s}{aN|Good bye!}
 * This format will create the following IChatComponents:
 *  - "Entity number " ; with DARK_GREEN color
 *  - %s               ; one of the parameters sent by the caller (as IChatComponent or
 *                       IChatFormat, since it's missing the "|" style delimiter character
 *  - " is the "       ; with DARK_GREEN color
 *  - %s               ; one of the parameters sent by the caller (as String since it HAS "|" style delimiter character)
 *  - "Good bye!"      ; with GREEN color and on another line. The modifier "N"
 *                     ; represents a new line BEFORE the component it's in
 *
 *  This ChatComponentFormatted will have the following structure:
 *      - sibling1     ; will be a list of all the elements before the component with
 *                     ; the "N" modifier in this case the first 3 components
 *      - sibling2     ; will be a list of the last component until the end
 */

public class TextComponentFormatted extends TextComponentList {

    private IChatComponent buffer = new TextComponentList();

    public TextComponentFormatted(String format, Object... args) {
        this(format, Arrays.asList(args).iterator());
    }

    public TextComponentFormatted(String format, Iterator args) {
        String[] components = StringUtils.split(format, "{}");

        for (String component : components) {
            processComponent(component, args);
        }
        this.appendSibling(buffer);
    }

    private void resetBuffer() {
        if (buffer.getSiblings().size() != 0) {
            this.appendSibling(buffer);
        }
        buffer = new TextComponentList();
    }

    private IChatComponent createComponent(String[] parts, Iterator args) {
        ChatStyle chatStyle = getStyle(parts[0]);
        String[] textWithHover = parts[1].split(" << ");
        String actualText = textWithHover[0];

        while (actualText.contains("%s")) {
            actualText = actualText.replaceFirst("%s", args.next().toString());
        }
        IChatComponent message = new ChatComponentText(actualText).setChatStyle(chatStyle);

        if (textWithHover.length == 2) {
            IChatComponent hoverText = new TextComponentFormatted("{" + textWithHover[1] + "}", args);
            chatStyle.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        }

        return message;
    }

    private void addComponent(Iterator args) {
        // TODO: Instead of %s use other identifiers for lists of elements or container (maybe)

        Object currArg = args.next();
        if (currArg instanceof IChatFormat) {
            buffer.appendSibling(((IChatFormat) currArg).toChatMessage());
        } else if (currArg instanceof IChatComponent) {
            buffer.appendSibling((IChatComponent) currArg);
        } else if (currArg instanceof TextComponentContainer) {
            resetBuffer();
            for (IChatComponent message : (TextComponentContainer) currArg) {
                this.appendSibling(message);
            }
        }
    }

    private void processComponent(String componentString, Iterator args) {
        String[] parts = componentString.split("\\|", 2);

        if (parts.length == 2) {
            if (parts[0].contains("N")) {
                resetBuffer();
            }
            buffer.appendSibling(createComponent(parts, args));
        } else if (parts.length == 1 && parts[0].equals("%s")){
            addComponent(args);
        } else {
            throw new FormatException("Format " + componentString + " is not valid. Valid format: {modifiers|text}");
        }
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
    public TextComponentFormatted applyDelimiter(String delimiter) {
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

    /**
     * Cut down version of the client-side only method for getting formatting code
     * from the ChatStyle class
     *
     * Why is this client side?
     */
    private String getFormattingCodeForStyle(ChatStyle style) {
        StringBuilder stringbuilder = new StringBuilder();

        if (style.getColor() != null)
        {
            stringbuilder.append(style.getColor());
        }

        if (style.getBold())
        {
            stringbuilder.append(EnumChatFormatting.BOLD);
        }

        if (style.getItalic())
        {
            stringbuilder.append(EnumChatFormatting.ITALIC);
        }

        if (style.getUnderlined())
        {
            stringbuilder.append(EnumChatFormatting.UNDERLINE);
        }

        if (style.getObfuscated())
        {
            stringbuilder.append(EnumChatFormatting.OBFUSCATED);
        }

        if (style.getStrikethrough())
        {
            stringbuilder.append(EnumChatFormatting.STRIKETHROUGH);
        }

        return stringbuilder.toString();

    }

    /**
     * Gets the formatted String for this component.
     * Example: {3|This is }{1|some text}
     * Will convert into: \u00a73This is \u00a71some text
     *                    \u00a7 - this is a unicode character used in Minecraft chat formatting
     */
    public String[] getLegacyFormattedText() {
        String[] result = new String[this.siblings.size()];
        int k = 0;

        for (IChatComponent component : (List<IChatComponent>) this.getSiblings()) {

            String actualText = "";
            for (IChatComponent subComponent : (List<IChatComponent>) component.getSiblings()) {
                actualText += getFormattingCodeForStyle(subComponent.getChatStyle());
                actualText += subComponent.getUnformattedText();
                actualText += EnumChatFormatting.RESET;
            }

            result[k++] = actualText;
        }

        return result;
    }
}
