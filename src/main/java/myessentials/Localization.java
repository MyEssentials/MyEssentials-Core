package myessentials;

import myessentials.chat.api.ChatComponentFormatted;
import myessentials.utils.ColorUtils;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and handles Localization files
 */
public class Localization {
    public static final String defaultLocalization = "en_US";

    private Map<String, String> localizations = new HashMap<String, String>();
    private Reader reader = null;

    public Localization(String filePath, String lang, String classPath, Class clazz) {
        try {
            InputStream is = null;

            if(filePath != null) {
                File file = new File(filePath + lang + ".lang");
                if (file.exists() && !file.isDirectory()) {
                    is = new FileInputStream(file);
                }
            }
            if (is == null) {
                is = clazz.getResourceAsStream(classPath + lang + ".lang");
            }
            if (is == null) {
                is = clazz.getResourceAsStream(classPath + defaultLocalization + ".lang");
                MyEssentialsCore.instance.LOG.warn("Reverting to en_US.lang because {} does not exist!", lang + ".lang");
            }

            reader = new InputStreamReader(is);
            load();
        } catch (Exception ex) {
            MyEssentialsCore.instance.LOG.warn("Failed to load localization for class " + clazz.getName() + "!", ex);
        }
    }

    /**
     * Do the actual loading of the Localization file
     */
    public void load() {
        BufferedReader br = new BufferedReader(reader);

        String line;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim(); // Trim it in-case there is spaces before the actual key-value pairs
                String[] entry = line.split("=");
                if (line.startsWith("#") || line.isEmpty() || entry.length < 2) {
                    // Ignore entries that are not formatted correctly (maybe log later)
                    // Ignore comments and empty lines
                    continue;
                }

                localizations.put(entry[0].trim(), entry[1].trim());
            }
            br.close();
        } catch (IOException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to load localization file!");
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    public IChatComponent getLocalization(String key, Object... args) {
        String localized = localizations.get(key);
        return localized == null ? new ChatComponentText(key) : new ChatComponentFormatted(localized, args);
    }

    public boolean hasLocalization(String key) {
        return localizations.containsKey(key);
    }


    public Map<String, String> getLocalizationMap() {
        return localizations;
    }
}