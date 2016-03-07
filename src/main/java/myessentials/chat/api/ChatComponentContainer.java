package myessentials.chat.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;

/**
 * A set of IChatComponents that can be sent as a whole
 */
public class ChatComponentContainer extends ArrayList<IChatComponent> {
    /**
     * Sends all chat components to the sender
     * @param sender
     */
    public void send(ICommandSender sender) {
        for (IChatComponent component : this) {
            sender.addChatMessage(component);
        }
    }
}
