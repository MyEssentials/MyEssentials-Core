package myessentials.event;

import net.minecraftforge.common.MinecraftForge;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired attempting to place an AE2 part or use a wrench.
 */
@Cancelable
public class AE2PartPlaceEvent extends Event {
    public final EntityPlayer player;
    public final int x;
    public final int y;
    public final int z;
    public final int face;
    public final World world;

    public AE2PartPlaceEvent(EntityPlayer player, int x, int y, int z, int face) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
        this.world = player.worldObj;
    }

    @SuppressWarnings("unused")
    public static boolean fireEvent(EntityPlayer player, int x, int y, int z, int face) {
        return MinecraftForge.EVENT_BUS.post(new AE2PartPlaceEvent(player, x, y, z, face));
    }
}
