package myessentials;

import myessentials.localization.api.LocalManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerTracker {

    @Listener
    public void onPlayerLogin(ClientConnectionEvent.Join ev) {
        ev.getTargetEntity().sendMessage(LocalManager.get("myessentials.test"));
    }

}
