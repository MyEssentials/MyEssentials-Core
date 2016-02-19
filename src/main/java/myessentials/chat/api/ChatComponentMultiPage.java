package myessentials.chat.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * A multi-page IChatComponent container.
 * Used for sending large amount of lines to a player.
 */
public class ChatComponentMultiPage extends ChatComponentContainer {

    private int maxComponentsPerPage = 10;

    public ChatComponentMultiPage(int maxComponentsPerPage) {
        this.maxComponentsPerPage = maxComponentsPerPage;
    }

    public void sendPage(ICommandSender sender, int page) {
        for (IChatComponent component : getHeader(page)) {
            sender.addChatMessage(component);
        }

        for (IChatComponent component : getPage(page)) {
            sender.addChatMessage(component);
        }
    }

    public List<IChatComponent> getHeader(int page) {
        List<IChatComponent> header = new ArrayList<IChatComponent>();

        header.add(new ChatComponentFormatted("{9| - MEC MultiPage Message - Page %s/%s}", page, getNumberOfPages()));

        return header;
    }

    public List<IChatComponent> getPage(int page) {
        return this.subList(maxComponentsPerPage * (page - 1), maxComponentsPerPage * page > size() ? size() : maxComponentsPerPage * page);
    }

    public int getNumberOfPages() {
        return (int) Math.ceil((float)size() / (float)maxComponentsPerPage);
    }

}
