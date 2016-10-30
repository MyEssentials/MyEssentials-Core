package myessentials.localization.api;

import myessentials.chat.api.TextComponentFormatted;
import net.minecraft.command.ICommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralized localization access
 */
public class LocalManager {

    private static Map<String, Local> localizations = new HashMap<String, Local>();

    /**
     * Registers a localization file to be used globally
     * The key string should be the first part of any localization key that is found in the file
     */
    public static void register(Local local, String key) {
        localizations.put(key, local);
    }

    /**
     * Finds the localized version that the key is pointing at and sends it to the ICommandSender
     */
    public static void send(ICommandSender sender, String localizationKey, Object... args) {
        sender.addChatMessage(get(localizationKey, args));
    }

    public static TextComponentFormatted get(String localizationKey, Object... args) {
        Local local = localizations.get(localizationKey.split("\\.")[0]);
        return local.getLocalization(localizationKey, args);
    }
}
