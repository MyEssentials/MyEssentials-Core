package myessentials.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Fired when an entity tramples a farmland
 */
@Cancelable
public class BlockTrampleEvent extends BlockEvent {
    /**
     * The entity that trampled the farm land
     */
    public final Entity entity;

    public BlockTrampleEvent(World world, BlockPos bp, IBlockState state, Entity entity) {
        super(world, bp, state);
        this.entity = entity;
    }

    @SuppressWarnings("unused")
    public static boolean fireEvent(World world, BlockPos bp, Entity entity) {
        return MinecraftForge.EVENT_BUS.post(new BlockTrampleEvent(world, bp, world.getBlockState(bp), entity));
    }
}
