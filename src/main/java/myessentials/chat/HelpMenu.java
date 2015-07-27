package myessentials.chat;

import myessentials.MyEssentialsCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.List;

public class HelpMenu {
    private static final JsonMessageBuilder messageBuilder = new JsonMessageBuilder();

    private int maxPageLines = 9;  // maxPageLines is 9 because 1 is taken up by the "top bar"
    private String name;
    private List<IChatComponent> lines = new ArrayList<IChatComponent>();

    public HelpMenu(String name) {
        this.name = name;
    }

    public void addLine(String line) {
        lines.add(new ChatComponentText(line));
    }

    public void addLineWithHoverText(String line, String hoverText) {
        messageBuilder.setText(line);
        messageBuilder.setHoverEventShowText(hoverText);
        lines.add(messageBuilder.build());
    }

    public void setMaxPageLines(int maxPageLines) {
        this.maxPageLines = maxPageLines;
    }

    public int getMaxPageLines() {
        return maxPageLines;
    }

    public void sendHelpPage(ICommandSender sender, int page) {

        int numberOfPages = (int) Math.ceil((double) (lines.size() / maxPageLines));

        MyEssentialsCore.instance.LOG.info(lines.size());
        MyEssentialsCore.instance.LOG.info(maxPageLines);
        MyEssentialsCore.instance.LOG.info((double) (lines.size() / maxPageLines));
        MyEssentialsCore.instance.LOG.info(Math.ceil((double) (lines.size() / maxPageLines)));

        if (page < 1)
            page = 1;
        if (page > numberOfPages)
            page = numberOfPages;

        sender.addChatMessage(new ChatComponentText(String.format("---------- %s Help (%s/%s) ----------", name.toUpperCase(), page, numberOfPages)));

        int start = getMaxPageLines()*(page-1);
        for(int i = 0; i < maxPageLines; i++) {
            sender.addChatMessage(lines.get(i + start));
        }
    }
}