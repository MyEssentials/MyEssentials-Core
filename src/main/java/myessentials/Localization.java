package myessentials;

import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and handles Localization files
 */

/**
 * Loads and handles Localization files
 */
public class Localization {
    public static final Map<Character, String> colorMap = new HashMap<Character, String>();
    public static final String defaultLocalization = "en_US";

    static {
        colorMap.put('0', "BLACK");
        colorMap.put('1', "DARK_BLUE");
        colorMap.put('2', "DARK_GREEN");
        colorMap.put('3', "DARK_AQUA");
        colorMap.put('4', "DARK_RED");
        colorMap.put('5', "DARK_PURPLE");
        colorMap.put('6', "GOLD");
        colorMap.put('7', "GRAY");
        colorMap.put('8', "DARK_GRAY");
        colorMap.put('9', "BLUE");
        colorMap.put('a', "GREEN");
        colorMap.put('b', "AQUA");
        colorMap.put('c', "RED");
        colorMap.put('d', "LIGHT_PURPLE");
        colorMap.put('e', "YELLOW");
        colorMap.put('f', "WHITE");
    }

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

    private String getLocalizationFromKey(String key) {
        String localized = localizations.get(key);

        if(localized != null) {
            for(int i = 0; i < localized.length(); i++) {
                if(localized.charAt(i) == '&') {
                    if(colorMap.get(localized.charAt(i+1)) != null && EnumChatFormatting.valueOf(colorMap.get(localized.charAt(i+1))) != null) {
                        localized = localized.substring(0, i) + EnumChatFormatting.valueOf(colorMap.get(localized.charAt(i + 1))) + localized.substring(i + 2);
                    } else {
                        localized = localized.substring(0, i) + localized.substring(i+2);
                    }
                }
            }
        }

        return localized == null ? key : localized;
    }

    /**
     * Returns the localized version of the given unlocalized key
     */
    public String getLocalization(String key, Object... args) {
        if (args.length > 0) {
            return String.format(getLocalizationFromKey(key), args);
        }
        else
            return getLocalizationFromKey(key);
    }

    public boolean hasLocalization(String key) {
        return localizations.containsKey(key);
    }


    public Map<String, String> getLocalizationMap() {
        return localizations;
    }
}