package myessentials.chat.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

/**
 * A multi-page IChatComponent container.
 * Used for sending large amount of lines to a player.
 */
public class TextComponentMultiPage extends TextComponentContainer {

    private int maxComponentsPerPage = 10;

    public TextComponentMultiPage(int maxComponentsPerPage) {
        this.maxComponentsPerPage = maxComponentsPerPage;
    }

    public void sendPage(ICommandSender sender, int page) {
        getHeader(page).send(sender);
        getPage(page).send(sender);
    }

    public TextComponentContainer getHeader(int page) {
        TextComponentContainer header = new TextComponentContainer();
        header.add(new TextComponentFormatted("{9| - MEC MultiPage Message - Page %s/%s}", page, getNumberOfPages()));

        return header;
    }

    public TextComponentContainer getPage(int page) {
        TextComponentContainer result = new TextComponentContainer();
        result.addAll(this.subList(maxComponentsPerPage * (page - 1), maxComponentsPerPage * page > size() ? size() : maxComponentsPerPage * page));
        return result;
    }

    public int getNumberOfPages() {
        return (int) Math.ceil((float)size() / (float)maxComponentsPerPage);
    }
}
