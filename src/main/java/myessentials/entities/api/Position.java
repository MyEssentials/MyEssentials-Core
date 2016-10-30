package myessentials.entities.api;

import myessentials.MyEssentialsCore;
import myessentials.chat.api.IChatFormat;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;

/**
 * Helper class for storing position
 * Please use this instead of the incredibly shitty one that Mojang has. Thanks.
 */
public class Position implements IChatFormat {
    private final int dim;
    private final double x;
    private final double y;
    private final double z;

    public Position(double x, double y, double z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public Position(BlockPos pos, int dim) {
        this(pos.getX(), pos.getY(), pos.getZ(), dim);
    }

    public Position(Entity entity) {
        this(entity.posX, entity.posY, entity.posZ, entity.dimension);
    }

    public Position(BlockSnapshot snapshot) {
        this(snapshot.getPos(), snapshot.getDimId());
    }

    public Position(TileEntity te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public int dim() {
        return dim;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public int xi() {
        return (int) x;
    }

    public int yi() {
        return (int) y;
    }

    public int zi() {
        return (int) z;
    }

    public Position offset(double offsetX, double offsetY, double offsetZ) {
        return new Position(x + offsetX, y + offsetY, z + offsetZ, dim);
    }

    public ChunkPos toChunkPos() {
        return new ChunkPos(dim, (int) x >> 4, (int) z >> 4);
    }

    @Override
    public String toString() {
        return toChatMessage().getUnformattedText();
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Position) {
            Position otherBP = (Position) other;
            return otherBP.dim == dim && otherBP.x == x && otherBP.y == y && otherBP.z == z;
        }
        return super.equals(other);
    }

    @Override
    public ITextComponent toChatMessage() {
        return MyEssentialsCore.instance.LOCAL.getLocalization("myessentials.format.blockpos", x, y, z, dim);
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }
}

