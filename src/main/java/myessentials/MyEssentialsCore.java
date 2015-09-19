package myessentials;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import myessentials.config.ConfigProcessor;
import myessentials.entities.tool.ToolManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = "MyEssentials-Core", name = "MyEssentials-Core", version = "@VERSION@", dependencies = "required-after:Forge", acceptableRemoteVersions = "*")
public class MyEssentialsCore {
    @Instance("MyEssentials-Core")
    public static MyEssentialsCore instance;

    public Logger LOG;
    public Configuration config;

    @EventHandler
    public void preinit(FMLPreInitializationEvent ev) {
        LOG = ev.getModLog();

        // Load Configs
        config = new Configuration(new File(ev.getModConfigurationDirectory(), "/MyTown/Core.cfg"));
        ConfigProcessor.load(config, Config.class);
        config.save();

        // Register handlers/trackers
        FMLCommonHandler.instance().bus().register(PlayerTracker.instance);
        MinecraftForge.EVENT_BUS.register(PlayerTracker.instance);

        FMLCommonHandler.instance().bus().register(ToolManager.instance);
        MinecraftForge.EVENT_BUS.register(ToolManager.instance);
    }
}