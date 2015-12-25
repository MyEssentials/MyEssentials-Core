package myessentials.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.World;

/**
 * Fired when a block is about to be modified.
 * If the event is canceled the block is not modified.
 */
@Cancelable
public class ModifyBlockEvent extends Event 
{
    public final int x;
    public final int y;
    public final int z;
    public final World world;

    public ModifyBlockEvent(World world, int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @SuppressWarnings("unused")
    public static boolean checkAndSetBlockToAir(World world, int x, int y, int z) {
        if (!MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, x, y, z))) {
            return world.setBlockToAir(x, y, z);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean checkAndSetBlock(World world, int x, int y, int z, Block block, int meta, int flags) {
        if (!MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, x, y, z))) {
            return world.setBlock(x, y, z, block, meta, flags);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean checkFlagAndBlock(boolean isCanceled, World world, int x, int y, int z) {
        if (!isCanceled) {
            isCanceled = MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, x, y, z));
        }
        return isCanceled;
    }
}
