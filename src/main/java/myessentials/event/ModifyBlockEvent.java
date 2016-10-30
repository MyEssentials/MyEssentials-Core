package myessentials.event;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired when a block is about to be modified.
 * If the event is canceled the block is not modified.
 */
@Cancelable
public class ModifyBlockEvent extends Event
{
    public final BlockPos bp;
    public final World world;

    public ModifyBlockEvent(World world, BlockPos bp) {
        this.bp = bp;
        this.world = world;
    }

    @SuppressWarnings("unused")
    public static boolean checkAndSetBlockToAir(World world, BlockPos bp) {
        if (!MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, bp))) {
            return world.setBlockToAir(bp);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean checkAndSetBlock(World world, BlockPos bp, IBlockState state, int flags) {
        if (!MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, bp))) {
            return world.setBlockState(bp, state, flags);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean checkFlagAndBlock(boolean isCanceled, World world, BlockPos bp) {
        if (!isCanceled) {
            isCanceled = MinecraftForge.EVENT_BUS.post(new ModifyBlockEvent(world, bp));
        }
        return isCanceled;
    }
}
