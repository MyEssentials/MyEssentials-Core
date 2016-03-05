package myessentials.entities.api;

import myessentials.MyEssentialsCore;
import myessentials.chat.api.ChatFormat;
import net.minecraft.util.IChatComponent;

/**
 * Helper class for storing position of a chunk
 */
public class ChunkPos extends ChatFormat {
    private final int dim;
    private final int x;
    private final int z;

    public ChunkPos(int dim, int x, int z) {
        this.dim = dim;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public String toString() {
        return toChatMessage().getUnformattedText();
    }

    @Override
    public IChatComponent toChatMessage(boolean shortened) {
        return MyEssentialsCore.instance.LOCAL.getLocalization("myessentials.format.chunkpos", x, z, dim);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChunkPos) {
            ChunkPos other = (ChunkPos) obj;
            return other.x == x && other.z == z && other.dim == dim;
        } else {
            return super.equals(obj);
        }
    }
}
