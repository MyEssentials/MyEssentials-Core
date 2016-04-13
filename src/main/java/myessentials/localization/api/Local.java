package myessentials.localization.api;

import myessentials.MyEssentialsCore;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.spongepowered.api.text.Text;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and handles Localization files
 */
public class Local {
    public static final String defaultLocalization = "en_US";

    private Map<String, String> localizations = new HashMap<String, String>();
    private String filePath;
    private String lang;
    private String classPath;
    private Class clazz;

    public Local(String filePath, String lang, String classPath, Class clazz) {
        this.filePath = filePath;
        this.lang = lang;
        this.classPath = classPath;
        this.clazz = clazz;

        load();
    }

    private Reader getReader() throws FileNotFoundException {
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

        return new InputStreamReader(is);
    }
    /**
     * Do the actual loading of the Localization file
     */
    public void load() {
        localizations.clear();

        try {
            BufferedReader br = new BufferedReader(getReader());
            String line;

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

    public Text getLocalization(String key, Object... args) {
        String localized = localizations.get(key);
        return Text.of(localized);
    }

    public boolean hasLocalization(String key) {
        return localizations.containsKey(key);
    }

    public Map<String, String> getLocalizationMap() {
        return localizations;
    }
}