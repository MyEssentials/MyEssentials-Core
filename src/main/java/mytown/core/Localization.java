package mytown.core;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.Sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads and handles Localization files
 * 
 * @author Joe Goett
 */
public class Localization {
	/**
	 * The localization map
	 */
	Map<String, String> localizations;
    public Map<Character, String> colorMap;
    Reader reader = null;

	/**
	 * Specifies the {@link Reader} to use when reading the localization
	 * 
	 * @param r
	 */
	public Localization(Reader r) {
		reader = r;
		localizations = new HashMap<String, String>();
        colorMap = new HashMap<Character, String>();
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

	/**
	 * Specifies the file to load via a {@link File}
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public Localization(File file) throws FileNotFoundException {
		this(new FileReader(file));
	}

	/**
	 * Specifies the file to load via a filename
	 * 
	 * @param filename
	 * @throws FileNotFoundException
	 */
	public Localization(String filename) throws FileNotFoundException {
		this(new File(filename));
	}

	/**
	 * Do the actual loading of the Localization file
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		BufferedReader br = new BufferedReader(reader);

		String line;
		while ((line = br.readLine()) != null) {
            line = line.trim(); // Trim it in-case there is spaces before the actual key-value pairs
			if (line.startsWith("#") || line.isEmpty()) {
				continue; // Ignore comments and empty lines
			}
			String[] entry = line.split("=");
			if (entry.length < 2) {
				continue; // Ignore entries that are not formatted correctly (maybe log later)
			}
			localizations.put(entry[0].trim(), entry[1].trim());
		}

		br.close();
	}

	private String getLocalizationFromKey(String key) {
		String localized = localizations.get(key);

        if(localized != null) {
            for(int i = 0; i < localized.length(); i++) {
                if(localized.charAt(i) == '&') {
                    if(EnumChatFormatting.valueOf(colorMap.get(localized.charAt(i+1))) != null) {
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
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public String getLocalization(String key, Object... args) {
		if (args.length > 0) {
            return String.format(getLocalizationFromKey(key), args);
        }
		else
			return getLocalizationFromKey(key);
	}

    public Map<String, String> getLocalizationMap() {
        return localizations;
    }
}