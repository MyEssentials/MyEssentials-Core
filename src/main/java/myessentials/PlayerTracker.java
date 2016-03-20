package myessentials;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import myessentials.chat.api.ChatManager;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.FakePlayer;

public class PlayerTracker {

    public static final PlayerTracker instance = new PlayerTracker();


    @SuppressWarnings("UnnecessaryReturnStatement")
    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent ev) {
        if (Config.instance.maintenanceMode.get() && ev.player instanceof EntityPlayerMP && !(ev.player instanceof FakePlayer)) {
            if (!PlayerUtils.isOp(ev.player)) {
                ((EntityPlayerMP) ev.player).playerNetServerHandler.kickPlayerFromServer(Config.instance.maintenanceModeMessage.get());
            }
            return;
        }
    }
}