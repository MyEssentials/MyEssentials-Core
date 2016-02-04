package myessentials.chat.core.style;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class PlainTextModifier implements ITextModifier {

    public PlainTextModifier() {
        //ChatUtils.sendChat(sender, localizedString, args);
        IChatComponent text = new ChatComponentText("text").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.BLACK)).appendText("asd");
    }


    @Override
    public IChatComponent apply(IChatComponent chatComponent) {
        return null;
    }
}
