package myessentials.entities.api;

import myessentials.MyEssentialsCore;
import myessentials.chat.api.ChatFormat;
import net.minecraft.util.IChatComponent;

/**
 * Helper class for storing position of a block
 */
public class BlockPos extends ChatFormat {
    private final int dim;
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return toChatMessage().getUnformattedText();
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof BlockPos) {
            BlockPos otherBP = (BlockPos) other;
            return otherBP.dim == dim && otherBP.x == x && otherBP.y == y && otherBP.z == z;
        }
        return super.equals(other);
    }

    @Override
    public IChatComponent toChatMessage(boolean shortened) {
        return MyEssentialsCore.instance.LOCAL.getLocalization("myessentials.format.blockpos", x, y, z, dim);
    }
}

