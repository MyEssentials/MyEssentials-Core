package myessentials.chat.api;

import myessentials.utils.ColorUtils;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentFormatted extends ChatComponentStyle {

    private String text;

    public ChatComponentFormatted(String format, IChatComponent... args) {

        String component = "";
        int argNumber = 0;
        this.appendSibling(new ChatComponentText(""));
        ChatStyle style = new ChatStyle();
        for (int i = 0; i < format.length(); i++) {

            if (format.charAt(i) == '&') {

                this.appendSibling(new ChatComponentText(component).setChatStyle(style));
                style = getStyleAtPosition(component, i);
                component = "";
                i++;
                if (format.charAt(i) == '[') {
                    while (format.charAt(i) != ']') {
                        i++;
                    }
                }
            } else if (format.charAt(i) == '%') {

                this.appendSibling(args[argNumber++]);

                component = "";
                i++;
            } else if (i == format.length() - 1) {

                this.appendSibling(new ChatComponentText(component));

            } else {
                component += format.charAt(i);
            }
        }
    }

    private ChatStyle getStyleAtPosition(String component, int position) {

        ChatStyle style = new ChatStyle();

        if (component.charAt(position + 1) == '[') {
            position++;
            while (component.charAt(position) != ']') {
                applyModifier(style, component.charAt(position));
                position++;
            }

        } else {
            applyModifier(style, component.charAt(position + 1));
        }

        return style;
    }

    private void applyModifier(ChatStyle style, char modifier) {

        if (modifier >= '0' && modifier <= '9' || modifier >= 'a' && modifier <= 'f') {
            style.setColor(ColorUtils.colorMap.get(modifier));
        } else {
            switch (modifier) {
                case 'k': style.setObfuscated(true); break;
                case 'l': style.setBold(true); break;
                case 'm': style.setStrikethrough(true); break;
                case 'n': style.setUnderlined(true); break;
                case 'o': style.setItalic(true); break;
            }
        }

    }

    @Override
    public String getUnformattedTextForChat() {
        return this.text;
    }

    @Override
    public IChatComponent createCopy() {
        return null;
    }
}
