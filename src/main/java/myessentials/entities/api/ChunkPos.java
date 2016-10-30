package myessentials.entities.api;

import myessentials.MyEssentialsCore;
import myessentials.chat.api.IChatFormat;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.BlockSnapshot;

/**
 * Helper class for storing position of a chunk
 */
public class ChunkPos implements IChatFormat {
    public final int dim;
    public final int x, z;

    public ChunkPos(int x, int z, int dim) {
        this.dim = dim;
        this.x = x;
        this.z = z;
    }

    public ChunkPos(BlockPos pos, int dim) {
        this(pos.getX() >> 4, pos.getZ() >> 4, dim);
    }

    public ChunkPos(Entity entity) {
        this(entity.chunkCoordX, entity.chunkCoordZ, entity.dimension);
    }

    public ChunkPos(BlockSnapshot snapshot) {
        this(snapshot.getPos(), snapshot.getDimId());
    }

    public ChunkPos(TileEntity te) {
        this(te.getPos(), te.getWorld().provider.getDimension());
    }

    public ChunkPos offset(int offsetX, int offsetZ) {
        return new ChunkPos(x + offsetX, z + offsetZ, dim);
    }

    public boolean isBlockIn(Position pos) {
        return pos.dim() == dim && pos.xi() >> 4 == x && pos.zi() >> 4 == z;
    }

    public Volume toVolume() {
        return new Volume(this);
    }

    @Override
    public String toString() {
        return toChatMessage().getUnformattedText();
    }

    @Override
    public ITextComponent toChatMessage() {
        return MyEssentialsCore.instance.LOCAL.getLocalization("myessentials.format.chunkpos", x, z, dim);
    }

    public NBTTagCompound toNBTTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", x);
        tag.setInteger("z", z);
        return tag;
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
