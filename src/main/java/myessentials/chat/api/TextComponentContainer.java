package myessentials.chat.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;

/**
 * A set of IChatComponents that can be sent as a whole
 */
public class TextComponentContainer extends ArrayList<ITextComponent> {
    /**
     * Sends all chat components to the sender
     */
    public void send(ICommandSender sender) {
        for (ITextComponent component : this) {
            sender.addChatMessage(component);
        }
    }
}
