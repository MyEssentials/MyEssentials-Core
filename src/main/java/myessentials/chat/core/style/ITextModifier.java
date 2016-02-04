package myessentials.chat.core.style;

import net.minecraft.util.IChatComponent;

public interface ITextModifier {

    IChatComponent apply(IChatComponent message);

}
