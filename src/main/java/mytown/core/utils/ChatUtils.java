package mytown.core.utils;

import mytown.core.Localization;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

/**
 * Useful methods for Chat
 */
public class ChatUtils {

    private ChatUtils() {

    }

    /**
     * Sends msg to sender.<br />
     * This method will split the message at newline chars (\n) and send each line as a separate message.
     */
    public static void sendChat(ICommandSender sender, String msg, Object... args) {
        String[] lines;
        if(args == null)
            lines = msg.split("\\\\n");
        else
            lines = String.format(msg, args).split("\\\\n");
        for (String line : lines) {
            sender.addChatMessage(new ChatComponentText(line));
        }
    }

    /**
     * Sends a localized msg to sender
     * @see ChatUtils#sendChat(net.minecraft.command.ICommandSender, String, Object...)
     */
    public static void sendLocalizedChat(ICommandSender sender, Localization local, String key, Object... args) {
        ChatUtils.sendChat(sender, local.getLocalization(key), args);
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