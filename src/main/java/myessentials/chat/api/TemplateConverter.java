package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextFormat;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TemplateConverter {

    public static List<TextTemplate> convert(String format) {

        List<TextElement> templateElements = new ArrayList<>();
        String[] components = StringUtils.split(format, "{}");

        for (String component : components) {
            processComponent(component);
        }
        TextTemplate.of();
        TextTemplate.of();
//        TextTemplate.arg();
        return null;
    }

    public static List<TextElement> processComponent(String component) {
        String[] parts = component.split("\\|", 2);
        List<TextElement> elements = new ArrayList<>();
        if (parts.length == 2) {
            elements.addAll(processStyle(parts[0]));
            String[] nonArgs = parts[1].split("%[^ ]*");
            for (String s : nonArgs) {
                elements.add(Text.of(s));
            }
        } else {

        }
        return elements;
    }

    public static List<TextElement> processStyle(String styleFormat) {
        List<TextElement> elements = new ArrayList<>();
        for (char c : styleFormat.toCharArray()) {
            elements.add(getStyle(c));
        }
        return elements;
    }

    public static TextElement getStyle(char modifier) {
        if (modifier >= '0' && modifier <= '9' || modifier >= 'a' && modifier <= 'f') {
            return ColorUtils.colorMap.get(modifier);
        }
        switch (modifier) {

            case 'k': return TextStyles.OBFUSCATED;
            case 'l': return TextStyles.BOLD;
            case 'm': return TextStyles.STRIKETHROUGH;
            case 'n': return TextStyles.UNDERLINE;
            case 'o': return TextStyles.ITALIC;
        }
        return TextFormat.NONE;
    }

    /*
    public ChatComponentFormatted(String format, Iterator args) {
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
        buffer = new ChatComponentList();
    }

    private Object createComponent(String[] parts, Iterator args) {
        ChatStyle chatStyle = getStyle(parts[0]);
        String[] textWithHover = parts[1].split(" << ");
        String actualText = textWithHover[0];

        while (actualText.contains("%s")) {
            actualText = actualText.replaceFirst("%s", args.next().toString());
        }
        IChatComponent message = new ChatComponentText(actualText).setChatStyle(chatStyle);

        if (textWithHover.length == 2) {
            IChatComponent hoverText = new ChatComponentFormatted("{" + textWithHover[1] + "}", args);
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
        } else if (currArg instanceof ChatComponentContainer) {
            resetBuffer();
            for (IChatComponent message : (ChatComponentContainer) currArg) {
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


    *//**
     * Converts the modifiers String to a ChatStyle
     * {modifiers|  some text}
     *  ^^^^^^^^    ^^^^^^^^^
     *  STYLE  for  THIS TEXT
     *//*
    private ChatStyle getStyle(String modifiers) {

        ChatStyle chatStyle = new ChatStyle();

        for (char c : modifiers.toCharArray()) {
            applyModifier(chatStyle, c);
        }

        return chatStyle;
    }

    *//**
     * Applies modifier to the style
     * Returns whether or not the modifier was valid
     *//*
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

    *//**
     * Adds a ChatComponentText between all of the siblings
     * This can be used for easily displaying a onHoverText on multiple lines
     *//*
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

    *//**
     * Cut down version of the client-side only method for getting formatting code
     * from the ChatStyle class
     *
     * Why is this client side?
     *//*
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

    *//**
     * Gets the formatted String for this component.
     * Example: {3|This is }{1|some text}
     * Will convert into: \u00a73This is \u00a71some text
     *                    \u00a7 - this is a unicode character used in Minecraft chat formatting
     *//*
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
    }*/

}
