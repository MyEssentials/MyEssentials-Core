package myessentials.chat.api;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ChatComponentList extends ChatComponentText {

    public ChatComponentList() {
        super("");
    }

    @Override
    public ChatStyle getChatStyle() {
        //if (this.getSiblings().size() == 1) {
        //    return ((IChatComponent) this.getSiblings().get(0)).getChatStyle();
        //} else {
            return super.getChatStyle();
        //}
    }
}
