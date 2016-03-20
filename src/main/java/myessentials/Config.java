package myessentials;

import myessentials.config.api.ConfigProperty;
import myessentials.config.api.ConfigTemplate;

public class Config extends ConfigTemplate {

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
}