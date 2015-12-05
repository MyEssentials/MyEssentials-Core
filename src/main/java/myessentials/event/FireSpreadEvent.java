package myessentials.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraft.world.World;

/**
 * Fired when fire is about to spread to another block.
 * If the event is canceled the block is not set to fire.
 */
@Cancelable
public class FireSpreadEvent extends BlockEvent 
{
    public FireSpreadEvent(World world, int x, int y, int z, Block block, int meta) {
        super(x, y, z, world, block, meta);
    }

    @SuppressWarnings("unused")
    public static boolean checkAndSetFire(World world, int x, int y, int z, Block block, int meta, int flags) {
        if (!MinecraftForge.EVENT_BUS.post(new FireSpreadEvent(world, x, y, z, block, meta))) {
            return world.setBlock(x, y, z, block, meta, flags);
        }
        return false;
    }
}
