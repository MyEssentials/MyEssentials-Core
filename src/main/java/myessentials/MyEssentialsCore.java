package myessentials;

import myessentials.entities.api.tool.ToolManager;
import myessentials.localization.api.Local;
import myessentials.localization.api.LocalManager;
import myessentials.utils.VisualsHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = "MyEssentials-Core", name = "MyEssentials-Core", version = "@VERSION@", dependencies = "required-after:Forge", acceptableRemoteVersions = "*")
public class MyEssentialsCore {
    @Mod.Instance("MyEssentials-Core")
    public static MyEssentialsCore instance;

    public Local LOCAL;
    public Logger LOG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent ev) {

        LOG = ev.getModLog();
        Constants.CONFIG_FOLDER = ev.getModConfigurationDirectory().getPath() + "/MyEssentials-Core/";
        Constants.DATABASE_FOLDER = ev.getModConfigurationDirectory().getParent() + "/databases/";
        // Load Configs
        Config.instance.init(Constants.CONFIG_FOLDER + "/Core.cfg", "MyEssentials-Core");
        // REF: The localization can simply take the whole config instance to get the localization needed.
        LOCAL = new Local(Constants.CONFIG_FOLDER + "/localization/", Config.instance.localization.get(), "/myessentials/localization/", MyEssentialsCore.class);
        LocalManager.register(LOCAL, "myessentials");

        // Register handlers/trackers
        MinecraftForge.EVENT_BUS.register(PlayerTracker.instance);
        MinecraftForge.EVENT_BUS.register(ToolManager.instance);
        MinecraftForge.EVENT_BUS.register(VisualsHandler.instance);
//        MinecraftForge.EVENT_BUS.register(SignManager.instance);
    }
}