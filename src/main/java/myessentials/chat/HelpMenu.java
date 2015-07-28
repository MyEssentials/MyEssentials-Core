package myessentials.chat;

import myessentials.MyEssentialsCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
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
        messageBuilder.setText(EnumChatFormatting.BLUE + line);
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

        int numberOfPages = (int) Math.ceil( (double)lines.size() / (double)maxPageLines);

        if (page < 1)
            page = 1;
        if (page > numberOfPages)
            page = numberOfPages;

        sender.addChatMessage(new ChatComponentText(String.format("%s------ %s Help (%s/%s) <Hover> ------", EnumChatFormatting.GREEN, name, page, numberOfPages)));

        int start = maxPageLines * (page-1);
        for(int i = start; i < Math.min(lines.size(), maxPageLines * page); i++) {
            sender.addChatMessage(lines.get(i));
        }
    }
}