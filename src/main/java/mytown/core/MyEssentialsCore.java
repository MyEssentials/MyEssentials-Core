package mytown.core;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import mytown.core.logger.Log;
import mytown.core.config.ConfigProcessor;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;

@Mod(modid = "MyEssentials-Core", name = "MyEssentials-Core", version = "2.0", dependencies = "required-after:Forge", acceptableRemoteVersions = "*")
public class MyEssentialsCore {
    @Instance("MyEssentials-Core")
    public static MyEssentialsCore instance;
    public static boolean isMCPC = false;

    public Log LOG;
    public Configuration config;

    @EventHandler
    public void preinit(FMLPreInitializationEvent ev) {
        LOG = new Log(ev.getModLog());

        // Load Configs
        config = new Configuration(new File(ev.getModConfigurationDirectory(), "/MyTown/Core.cfg"));
        ConfigProcessor.load(config, Config.class);
        config.save();

        // Register handlers/trackers
        FMLCommonHandler.instance().bus().register(new PlayerTracker());
    }

    @EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent ev) {
        //Used to decide side to prevent this from erroring out if someone decides to use this on a client (eg development testing)
        if(ev.getSide() == Side.SERVER)
            MyEssentialsCore.isMCPC = ev.getServer().getServerModName().contains("mcpc");
    }
}