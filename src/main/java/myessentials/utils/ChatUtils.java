package myessentials.utils;

import myessentials.MyEssentialsCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.HashMap;
import java.util.Map;
/**
 * Useful methods for Chat
 */
public class ChatUtils {

    private ChatUtils() {

    }

    /**
<<<<<<< HEAD
     * Maps chat formatting by it's code
     */
    private static final Map<Character, EnumChatFormatting> formattingMap = new HashMap<Character, EnumChatFormatting>(22);
    static {
        for(EnumChatFormatting formatting: EnumChatFormatting.values()) {
            formattingMap.put(formatting.getFormattingCode(), formatting);
        }
    }



    public static void sendChat(ICommandSender sender, IChatComponent message) {

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
    public static ChatComponentText chatComponentFromLegacyText(String message) {
        ChatComponentText base;
        String[] parts = message.split(Character.toString('\u00A7'));
        if(parts.length == 1)
            return new ChatComponentText(message);

        base = new ChatComponentText(parts[0]);

        ChatStyle chatStyle = new ChatStyle();
        for(int i = 1; i < parts.length; i++) {
            String current = parts[i];
            char code = current.charAt(0);
            String text = current.substring(1);

            if(code >= '0' && code <= '9' || code >= 'a' && code <= 'f' || code == 'r') {
                chatStyle = new ChatStyle();
                chatStyle.setColor(formattingMap.get(code));
            }
            else {
                chatStyle = chatStyle.createDeepCopy();
                switch (code) {
                    case 'k': chatStyle.setObfuscated(true); break;
                    case 'l': chatStyle.setBold(true); break;
                    case 'm': chatStyle.setStrikethrough(true); break;
                    case 'n': chatStyle.setUnderlined(true); break;
                    case 'o': chatStyle.setItalic(true); break;
                }
            }

            base.appendSibling(new ChatComponentText(text).setChatStyle(chatStyle));
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
