package myessentials.utils;

import myessentials.entities.api.ChunkPos;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * All utilities that are exclusively for in world objects go here.
 */
public class WorldUtils {

    private WorldUtils() {
    }

    /**
     * Transforms a box made out of actual coordinates to a list of all the chunks that this box is in
     */
    public static List<ChunkPos> getChunksInBox(int dim, int minX, int minZ, int maxX, int maxZ) {
        List<ChunkPos> list = new ArrayList<ChunkPos>();
        for (int i = minX >> 4; i <= maxX >> 4; i++) {
            for (int j = minZ >> 4; j <= maxZ >> 4; j++) {
                list.add(new ChunkPos(dim, i, j));
            }
        }
        return list;
    }

    /**
     * Drops the specified itemstack in the worls as an EntityItem
     */
    public static void dropAsEntity(World world, int x, int y, int z, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        double f = 0.7D;
        double dx = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
        double dy = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
        double dz = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;

        EntityItem entityItem = new EntityItem(world, x + dx, y + dy, z + dz, itemStack);
        world.spawnEntityInWorld(entityItem);
    }

    /**
     * Returns the first block from top to bottom that is considered not opaque
     */
    public static int getMaxHeightWithSolid(int dim, int x, int z) {
        World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);
        int y = world.getActualHeight();
        BlockPos bp = new BlockPos(x, y, z);
        while(!world.getBlockState(bp).getMaterial().isOpaque() && y > 0) {
            bp.add(0, -1, 0);
        }
        return y;
    }

    /**
     * General method for getting Minecraft Server instance
     */
    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    /**
     * Return the WorldServer with the given id
     */
    public static WorldServer getWorld(int id) {
        return getServer().worldServerForDimension(id);
    }
}
