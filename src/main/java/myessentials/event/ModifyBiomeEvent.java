package myessentials.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.World;

/**
 * Fired when a biome is about to be modified.
 * If the event is canceled the biome is not modified.
 */
@Cancelable
public class ModifyBiomeEvent extends Event 
{
    public final int x;
    public final int z;
    public final World world;

    public ModifyBiomeEvent(World world, int x, int z) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    @SuppressWarnings("unused")
    public static boolean checkBiome(World world, int x, int z) {
        return MinecraftForge.EVENT_BUS.post(new ModifyBiomeEvent(world, x, z));
    }
}
