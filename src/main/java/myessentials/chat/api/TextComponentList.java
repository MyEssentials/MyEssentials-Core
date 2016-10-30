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
        //    return ((ITextComponent) this.getSiblings().get(0)).getStyle();
        //} else {
            return super.getStyle();
        //}
    }
}
