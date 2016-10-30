package myessentials.chat.api;

import myessentials.localization.api.LocalManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

public class ChatManager {

    /**
     * Global method for sending localized messages
     */
    public static void send(ICommandSender sender, String localizationKey, Object... args) {
        send(sender, LocalManager.get(localizationKey, args));
    }

    /**
     * Global method for sending messages
     * If the message sent is a ChatComponentList then only its siblings are sent, omitting the root component
     */
    @SuppressWarnings("unchecked")
    public static void send(ICommandSender sender, ITextComponent message) {
        if (sender == null) {
            return;
        }

        if (message instanceof TextComponentList) {
            for (ITextComponent sibling : message.getSiblings()) {
                sender.addChatMessage(sibling);
            }
        } else {
            sender.addChatMessage(message);
        }
    }
}
