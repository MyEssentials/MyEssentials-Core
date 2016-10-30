package myessentials.utils;


import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Colors for characters or types of things commonly used.
 */
public class ColorUtils {

    public static final Style stylePlayer = new Style().setColor(TextFormatting.WHITE);
    public static final Style styleOwner = new Style().setColor(TextFormatting.RED);
    public static final Style styleTown = new Style().setColor(TextFormatting.GOLD);
    public static final Style styleSelectedTown = new Style().setColor(TextFormatting.GREEN);
    public static final Style styleInfoText = new Style().setColor(TextFormatting.GRAY);
    public static final Style styleConfigurableFlag = new Style().setColor(TextFormatting.GRAY);
    public static final Style styleUnconfigurableFlag = new Style().setColor(TextFormatting.DARK_GRAY);
    public static final Style styleValueFalse = new Style().setColor(TextFormatting.RED);
    public static final Style styleValueRegular = new Style().setColor(TextFormatting.GREEN);
    public static final Style styleDescription = new Style().setColor(TextFormatting.GRAY);
    public static final Style styleCoords = new Style().setColor(TextFormatting.BLUE);
    public static final Style styleComma = new Style().setColor(TextFormatting.WHITE);
    public static final Style styleEmpty = new Style().setColor(TextFormatting.RED);
    public static final Style styleAdmin = new Style().setColor(TextFormatting.RED);
    public static final Style styleGroupType = new Style().setColor(TextFormatting.BLUE);
    public static final Style styleGroupText = new Style().setColor(TextFormatting.GRAY);
    public static final Style styleGroupParents = new Style().setColor(TextFormatting.WHITE);
    public static final Style styleGroup = new Style().setColor(TextFormatting.BLUE);

    public static final Map<Character, TextFormatting> colorMap = new HashMap<Character, TextFormatting>();
    static {
        ColorUtils.colorMap.put('0', TextFormatting.BLACK);
        ColorUtils.colorMap.put('1', TextFormatting.DARK_BLUE);
        ColorUtils.colorMap.put('2', TextFormatting.DARK_GREEN);
        ColorUtils.colorMap.put('3', TextFormatting.DARK_AQUA);
        ColorUtils.colorMap.put('4', TextFormatting.DARK_RED);
        ColorUtils.colorMap.put('5', TextFormatting.DARK_PURPLE);
        ColorUtils.colorMap.put('6', TextFormatting.GOLD);
        ColorUtils.colorMap.put('7', TextFormatting.GRAY);
        ColorUtils.colorMap.put('8', TextFormatting.DARK_GRAY);
        ColorUtils.colorMap.put('9', TextFormatting.BLUE);
        ColorUtils.colorMap.put('a', TextFormatting.GREEN);
        ColorUtils.colorMap.put('b', TextFormatting.AQUA);
        ColorUtils.colorMap.put('c', TextFormatting.RED);
        ColorUtils.colorMap.put('d', TextFormatting.LIGHT_PURPLE);
        ColorUtils.colorMap.put('e', TextFormatting.YELLOW);
        ColorUtils.colorMap.put('f', TextFormatting.WHITE);
    }

    public static Character getColorCode(TextFormatting format) {
        for (Map.Entry<Character, TextFormatting> entry : colorMap.entrySet()) {
            if (entry.getValue() == format) {
                return entry.getKey();
            }
        }
        return null;
    }
}
