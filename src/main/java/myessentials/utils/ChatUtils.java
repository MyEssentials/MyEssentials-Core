package myessentials.utils;

import myessentials.Localization;
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
        if (sender == null) return;
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
}
