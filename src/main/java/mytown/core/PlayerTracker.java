package mytown.core;

import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerTracker {
	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent ev) {
		if (Config.maintenanceMode && ev.player instanceof EntityPlayerMP && !(ev.player instanceof FakePlayer)) {
			((EntityPlayerMP) ev.player).playerNetServerHandler.kickPlayerFromServer(Config.maintenanceModeMessage);
			return;
		}
	}
}