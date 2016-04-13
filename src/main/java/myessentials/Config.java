package myessentials;

import com.google.inject.Inject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.config.DefaultConfig;

public class Config {

    @Inject
    @DefaultConfig(sharedRoot = false)
    private static ConfigurationLoader<CommentedConfigurationNode> loader;

    public static CommentedConfigurationNode maintananceMode;
    public static CommentedConfigurationNode maintananceModeMessage;
    public static CommentedConfigurationNode localization;

    public static void init() {
        ConfigurationOptions options = ConfigurationOptions.defaults();
        options.setShouldCopyDefaults(true);
        ConfigurationNode root = loader.createEmptyNode(options);

        maintananceMode = (CommentedConfigurationNode) root.getNode("general", "maintananceMode");
        maintananceMode.setValue(false);
        maintananceMode.setComment("Allows toggling maintenance mode");
        maintananceModeMessage = (CommentedConfigurationNode) root.getNode("general", "maintananceModeMessage");
        maintananceModeMessage.setValue("Server is in maintenance mode currently. Please come back later.");
        maintananceModeMessage.setComment("Custom message to display when in maintenance mode");
        localization = (CommentedConfigurationNode) root.getNode("general", "localization");
        localization.setValue("en_US");
        localization.setComment("Localization file without file extension. Loaded from config/MyTown/localization/ first, then from the jar, then finally will fallback to en_US if needed.");
    }

    /*
    public static final Config instance = new Config();

    public ConfigProperty<Boolean> maintenanceMode = new ConfigProperty<Boolean>(
            "maintenanceMode", "general",
            "Allows toggling maintenance mode",
            false);

    public ConfigProperty<String> maintenanceModeMessage = new ConfigProperty<String>(
            "maintenanceModeMessage", "general",
            "Custom message to display when in maintenance mode",
            "Server is in maintenance mode currently. Please come back later.");

    public ConfigProperty<String> localization = new ConfigProperty<String>(
            "localization", "general",
            "Localization file without file extension.\\nLoaded from config/MyTown/localization/ first, then from the jar, then finally will fallback to en_US if needed.",
            "en_US");
    */
}