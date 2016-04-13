package myessentials;

import com.google.inject.Inject;
import myessentials.localization.api.Local;
import myessentials.localization.api.LocalManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigRoot;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "com.myessentials", name = "MyEssentials-Core", version = "@VERSION@", description = "MyEssentials core mod")
public class MyEssentialsCore {
    public static MyEssentialsCore instance;

    public Local LOCAL;

    @Inject
    public Logger LOG;
    public ConfigRoot config;

    @Listener
    public void preInit(GamePreInitializationEvent ev) {
        config = Sponge.getGame().getConfigManager().getPluginConfig(this);
        Constants.CONFIG_FOLDER = config.getDirectory().toString();
        Constants.DATABASE_FOLDER = Constants.CONFIG_FOLDER + "/databases/";
        // Load Configs
//        Config.instance.init(Constants.CONFIG_FOLDER + "/Core.cfg", "MyEssentials-Core");
        // REF: The localization can simply take the whole config instance to get the localization needed.
//        LOCAL = new Local(Constants.CONFIG_FOLDER + "/localization/", Config.instance.localization.get(), "/myessentials/localization/", MyEssentialsCore.class);
        LocalManager.register(LOCAL, "myessentials");

        // Register handlers/trackers
//        Sponge.getEventManager().registerListeners(this, PlayerTracker.instance);
//        Sponge.getEventManager().registerListeners(this, ToolManager.instance);
//        Sponge.getEventManager().registerListeners(this, SignManager.instance);
    }
}