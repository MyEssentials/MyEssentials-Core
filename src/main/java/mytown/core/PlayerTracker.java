package mytown.core;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class PlayerTracker {
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent ev) {
		if (Config.maintenanceMode && ev.player instanceof EntityPlayerMP && !(ev.player instanceof FakePlayer)) {
			((EntityPlayerMP) ev.player).playerNetServerHandler.kickPlayerFromServer(Config.maintenanceModeMessage);
			return;
		}
	}
}