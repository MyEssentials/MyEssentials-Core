package myessentials.chat.api;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class HelpMenu {
    private static final JsonMessageBuilder messageBuilder = new JsonMessageBuilder();

    private int maxPageLines = 9;
    private String name;
    private List<ChatLine> lines = new ArrayList<ChatLine>();

    private EnumChatFormatting colorTitle = EnumChatFormatting.BLUE;
    private EnumChatFormatting colorShownText = EnumChatFormatting.GRAY;
    private EnumChatFormatting colorHoverText = EnumChatFormatting.GREEN;

    public HelpMenu(String name) {
        this.name = name;
    }

    public void addLine(String line) {
        lines.add(new ChatLine(line, null));
    }

    public void addLineWithHoverText(String line, String hoverText) {
        lines.add(new ChatLine(line, hoverText));
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

        if(sender instanceof EntityPlayer) {
            sender.addChatMessage(new ChatComponentText(String.format("%s - MEC Command Help - Page %s/%s - OnHover", colorTitle, page, numberOfPages)));
            sender.addChatMessage(new ChatComponentText(String.format("%s - Command: %s%s", colorTitle, EnumChatFormatting.ITALIC, name)));

            int start = maxPageLines * (page - 1);
            for (int i = start; i < Math.min(lines.size(), maxPageLines * page); i++) {
                ChatLine currentLine = lines.get(i);
                messageBuilder.setText(colorShownText + currentLine.shownText);
                if(currentLine.hasHoverText()) {
                    messageBuilder.setHoverEventShowText(colorHoverText + currentLine.onHoverText);
                    //messageBuilder.setHoverEventShowText("This is a test ... \n jkasbdnuasbdasunduasdkiusaghdaasjbndjksabdskjhbakuashauaguiasgsifuagaiuasgsafiusfgsaisaisfgas");
                } else {
                    messageBuilder.resetHoverEvent();
                }
                sender.addChatMessage(messageBuilder.build());
            }
        } else {
            sender.addChatMessage(new ChatComponentText(String.format(" - MEC Command Help - Page %s/%s - PlainText", page, numberOfPages)));
            sender.addChatMessage(new ChatComponentText(String.format(" - Command: [%s]", name)));

            int start = maxPageLines * (page - 1);
            for (int i = start; i < Math.min(lines.size(), maxPageLines * page); i++) {
                ChatLine currentLine = lines.get(i);
                sender.addChatMessage(new ChatComponentText(currentLine.shownText + (currentLine.hasHoverText() ? ": " + currentLine.onHoverText : "")));
            }
        }
    }

    private class ChatLine {
        public String shownText, onHoverText;

        public ChatLine(String shownText, String onHoverText) {
            this.shownText = shownText;
            this.onHoverText = onHoverText;
        }

        public boolean hasHoverText() {
            return onHoverText != null;
        }
    }
}