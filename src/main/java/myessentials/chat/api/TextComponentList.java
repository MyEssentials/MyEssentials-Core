package myessentials.chat.api;

import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;

public class TextComponentList extends TextComponentString {

    public TextComponentList() {
        super("");
    }

    @Override
    public Style getStyle() {
        //if (this.getSiblings().size() == 1) {
        //    return ((IChatComponent) this.getSiblings().get(0)).getChatStyle();
        //} else {
            return super.getStyle();
        //}
    }
}
