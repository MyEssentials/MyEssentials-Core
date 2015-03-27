package mytown.core;

import java.io.File;

import mytown.core.utils.Log;
import mytown.core.utils.config.ConfigProcessor;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "MyEssentials-Core", name = "MyEssentials-Core", version = "2.0", dependencies = "required-after:Forge", acceptableRemoteVersions = "*")
public class MyEssentialsCore {
	@Instance("MyEssentials-Core")
	public static MyEssentialsCore Instance;
	public static boolean IS_MCPC = false;

	public Log log;
	public Configuration config;

	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent ev) {
		log = new Log(ev.getModLog());

		// Load Configs
		config = new Configuration(new File(ev.getModConfigurationDirectory(), "/MyTown/Core.cfg"));
		ConfigProcessor.load(config, Config.class);
		config.save();

		// Register handlers/trackers
		FMLCommonHandler.instance().bus().register(new PlayerTracker());
	}

}