package myessentials.chat.api;


import net.minecraft.util.IChatComponent;

/**
 * Represents an object type that can be converted into a chat message or a part of a chat message
 */
public abstract class IChatFormat {

    public abstract IChatComponent toChatMessage(boolean shortened);

    public final IChatComponent toChatMessage() {
        return toChatMessage(false);
    }

}
