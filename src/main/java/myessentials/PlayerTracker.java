package myessentials;

import myessentials.chat.JsonMessageBuilder;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.init.Items;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class PlayerTracker {

    public static final PlayerTracker instance = new PlayerTracker();


    @SuppressWarnings("UnnecessaryReturnStatement")
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent ev) {
        if (Config.maintenanceMode && ev.player instanceof EntityPlayerMP && !(ev.player instanceof FakePlayer)) {
            if (!PlayerUtils.isOp(ev.player)) {
                ((EntityPlayerMP) ev.player).playerNetServerHandler.kickPlayerFromServer(Config.maintenanceModeMessage);
            }
            return;
        }
    }
}