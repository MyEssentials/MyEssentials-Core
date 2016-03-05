package myessentials.chat.api;

import net.minecraft.util.IChatComponent;

public abstract class ChatFormat implements IChatFormat {
    @Override
    public IChatComponent toChatMessage() {
        return toChatMessage(false);
    }
}
