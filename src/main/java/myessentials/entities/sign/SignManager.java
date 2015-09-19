package myessentials.entities.sign;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import myessentials.entities.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

public class SignManager {

    public static final SignManager instance = new SignManager();

    public final Sign.Container signs = new Sign.Container();

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if(!(ev.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Sign sign = signs.get(new BlockPos(ev.x, ev.y, ev.z, ev.world.provider.dimensionId));
        if(sign == null) {
            return;
        }

        if(sign.getTileEntity() == null) {
            signs.remove(sign);
            return;
        }

        if(ev.entityPlayer.isSneaking()) {
            sign.onShiftRightClick(ev.entityPlayer);
        } else {
            sign.onRightClick(ev.entityPlayer);
        }
    }

    @SubscribeEvent
    public void onPlayerBreaksBlock(BlockEvent.BreakEvent ev) {
        Sign sign = signs.get(new BlockPos(ev.x, ev.y, ev.z, ev.world.provider.dimensionId));

        if(sign != null) {
            sign.onShiftRightClick(ev.getPlayer());
            ev.setCanceled(true);
        }
    }
}
