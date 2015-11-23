package myessentials.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class BlockTrampleEvent extends BlockEvent {
    public final Entity entity;

    public BlockTrampleEvent(Entity entity, int x, int y, int z, Block block, int blockMetadata) {
        super(x, y, z, entity.worldObj, block, blockMetadata);
        this.entity = entity;
    }

    public static boolean fireEvent(Entity entity, BlockFarmland block, int x, int y, int z) {
        return MinecraftForge.EVENT_BUS.post(new BlockTrampleEvent(entity, x, y, z, block, entity.worldObj.getBlockMetadata(x, y, z)));
    }
}
