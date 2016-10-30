package myessentials.chat.api;

import myessentials.exception.FormatException;
import myessentials.utils.ColorUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An ITextComponent that converts a format to a set of ITextComponents
 *
 * Each of the parenthesis pairs represent an ITextComponent with its
 * own Style (color, bold, underlined, etc.)
 *
 * Example: {2|Entity number }{%s}{2| is the }{7l| %s}{aN|Good bye!}
 * This format will create the following ITextComponents:
 *  - "Entity number " ; with DARK_GREEN color
 *  - %s               ; one of the parameters sent by the caller (as ITextComponent or
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

    private ITextComponent buffer = new TextComponentList();

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

    private ITextComponent createComponent(String[] parts, Iterator args) {
        Style Style = getStyle(parts[0]);
        String[] textWithHover = parts[1].split(" << ");
        String actualText = textWithHover[0];

        while (actualText.contains("%s")) {
            actualText = actualText.replaceFirst("%s", args.next().toString());
        }
        ITextComponent message = new TextComponentString(actualText).setStyle(Style);

        if (textWithHover.length == 2) {
            ITextComponent hoverText = new TextComponentFormatted("{" + textWithHover[1] + "}", args);
            Style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText));
        }

        return message;
    }

    private void addComponent(Iterator args) {
        // TODO: Instead of %s use other identifiers for lists of elements or container (maybe)

        Object currArg = args.next();
        if (currArg instanceof IChatFormat) {
            buffer.appendSibling(((IChatFormat) currArg).toChatMessage());
        } else if (currArg instanceof ITextComponent) {
            buffer.appendSibling((ITextComponent) currArg);
        } else if (currArg instanceof TextComponentContainer) {
            resetBuffer();
            for (ITextComponent message : (TextComponentContainer) currArg) {
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
     * Converts the modifiers String to a Style
     * {modifiers|  some text}
     *  ^^^^^^^^    ^^^^^^^^^
     *  STYLE  for  THIS TEXT
     */
    private Style getStyle(String modifiers) {

        Style Style = new Style();

        for (char c : modifiers.toCharArray()) {
            applyModifier(Style, c);
        }

        return Style;
    }

    /**
     * Applies modifier to the style
     * Returns whether or not the modifier was valid
     */
    private boolean applyModifier(Style Style, char modifier) {
        if (modifier >= '0' && modifier <= '9' || modifier >= 'a' && modifier <= 'f') {
            Style.setColor(ColorUtils.colorMap.get(modifier));
            return true;
        }
        switch (modifier) {
            case 'k': Style.setObfuscated(true); return true;
            case 'l': Style.setBold(true); return true;
            case 'm': Style.setStrikethrough(true); return true;
            case 'n': Style.setUnderlined(true); return true;
            case 'o': Style.setItalic(true); return true;
        }
        return false;
    }

    /**
     * Adds a TextComponentString between all of the siblings
     * This can be used for easily displaying a onHoverText on multiple lines
     */
    public TextComponentFormatted applyDelimiter(String delimiter) {
        List<ITextComponent> newSiblings = new ArrayList<ITextComponent>();
        for (ITextComponent component : (List<ITextComponent>) siblings) {
            if (newSiblings.size() > 0) {
                newSiblings.add(new TextComponentString(delimiter));
            }
            newSiblings.add(component);
        }
        this.siblings = newSiblings;
        return this;
    }

    /**
     * Cut down version of the client-side only method for getting formatting code
     * from the Style class
     *
     * Why is this client side?
     */
    private String getFormattingCodeForStyle(Style style) {
        StringBuilder stringbuilder = new StringBuilder();

        if (style.getColor() != null)
        {
            stringbuilder.append(style.getColor());
        }

        if (style.getBold())
        {
            stringbuilder.append(TextFormatting.BOLD);
        }

        if (style.getItalic())
        {
            stringbuilder.append(TextFormatting.ITALIC);
        }

        if (style.getUnderlined())
        {
            stringbuilder.append(TextFormatting.UNDERLINE);
        }

        if (style.getObfuscated())
        {
            stringbuilder.append(TextFormatting.OBFUSCATED);
        }

        if (style.getStrikethrough())
        {
            stringbuilder.append(TextFormatting.STRIKETHROUGH);
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

        for (ITextComponent component : this.getSiblings()) {

            String actualText = "";
            for (ITextComponent subComponent : component.getSiblings()) {
                actualText += getFormattingCodeForStyle(subComponent.getStyle());
                actualText += subComponent.getUnformattedText();
                actualText += TextFormatting.RESET;
            }

            result[k++] = actualText;
        }

        return result;
    }
}
