package mytown.core;

import mytown.core.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerTracker {
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