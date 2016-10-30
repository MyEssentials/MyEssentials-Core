package myessentials.utils;

import myessentials.MyEssentialsCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;
/**
 * Useful methods for Chat
 */
public class ChatUtils {

    private ChatUtils() { }

    /**
     * Maps chat formatting by it's code
     */
    private static final Map<Character, TextFormatting> formattingMap = new HashMap<Character, TextFormatting>(22);
    static {
        for(TextFormatting formatting: TextFormatting.values()) {
            formattingMap.put(formatting.toString().charAt(1), formatting);
        }

    }

    public static void sendChat(ICommandSender sender, ITextComponent message) {

        sender.addChatMessage(message);

    }

    public static void sendChat2(ICommandSender sender, String message, Object... args) {

    }

    /**
     * Sends msg to sender.<br />
     * This method will split the message at newline chars (\n) and send each line as a separate message.
     */
    public static void sendChat(ICommandSender sender, String msg, Object... args) {
        if (sender == null) return;
        String[] lines;
        if(args == null) {
            lines = msg.split("\\\\n");
        } else {
            lines = String.format(msg, args).split("\\\\n");
        }
        try {
            for (String line : lines) {
                sender.addChatMessage(chatComponentFromLegacyText(line));
            }
        } catch (Exception ex) {
            MyEssentialsCore.instance.LOG.error("Failed to send chat message! Message: {}", msg);
        }
    }

    /**
     * Parses a legacy text
     * @param message A formatted legacy text
     * @return The parsed text
     */
    public static TextComponentString chatComponentFromLegacyText(String message) {
        TextComponentString base;
        String[] parts = message.split(Character.toString('\u00A7'));
        if(parts.length == 1)
            return new TextComponentString(message);

        base = new TextComponentString(parts[0]);

        Style Style = new Style();
        for(int i = 1; i < parts.length; i++) {
            String current = parts[i];
            char code = current.charAt(0);
            String text = current.substring(1);

            if(code >= '0' && code <= '9' || code >= 'a' && code <= 'f' || code == 'r') {
                Style = new Style();
                Style.setColor(formattingMap.get(code));
            }
            else {
                Style = Style.createDeepCopy();
                switch (code) {
                    case 'k': Style.setObfuscated(true); break;
                    case 'l': Style.setBold(true); break;
                    case 'm': Style.setStrikethrough(true); break;
                    case 'n': Style.setUnderlined(true); break;
                    case 'o': Style.setItalic(true); break;
                }
            }

            base.appendSibling(new TextComponentString(text).setStyle(Style));
        }

        return base;
    }

    /**
     * Returns true if arg equals on, enable, true, or t. False otherwise.
     */
    // TODO: Overside equal maybe?
    // TODO Change name/change location?
    public static boolean equalsOn(String arg, boolean caseSensitive) {
        if (!caseSensitive)
            arg = arg.toLowerCase();
        return "on".equals(arg) || "enable".equals(arg) || "true".equals(arg) || "t".equals(arg);
    }

    /**
     * Same as {@link ChatUtils#equalsOn(String, boolean)}, but is not case sensitive
     */
    public static boolean equalsOn(String arg) {
        return equalsOn(arg, false);
    }
}
