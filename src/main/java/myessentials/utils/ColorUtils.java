package myessentials.utils;

import com.google.common.collect.ImmutableMap;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;
import java.util.Map;

public class ColorUtils {
    public static final Map<Character, TextColor> colorMap = new HashMap<>();
    static {
        ColorUtils.colorMap.put('0', TextColors.BLACK);
        ColorUtils.colorMap.put('1', TextColors.DARK_BLUE);
        ColorUtils.colorMap.put('2', TextColors.DARK_GREEN);
        ColorUtils.colorMap.put('3', TextColors.DARK_AQUA);
        ColorUtils.colorMap.put('4', TextColors.DARK_RED);
        ColorUtils.colorMap.put('5', TextColors.DARK_PURPLE);
        ColorUtils.colorMap.put('6', TextColors.GOLD);
        ColorUtils.colorMap.put('7', TextColors.GRAY);
        ColorUtils.colorMap.put('8', TextColors.DARK_GRAY);
        ColorUtils.colorMap.put('9', TextColors.BLUE);
        ColorUtils.colorMap.put('a', TextColors.GREEN);
        ColorUtils.colorMap.put('b', TextColors.AQUA);
        ColorUtils.colorMap.put('c', TextColors.RED);
        ColorUtils.colorMap.put('d', TextColors.LIGHT_PURPLE);
        ColorUtils.colorMap.put('e', TextColors.YELLOW);
        ColorUtils.colorMap.put('f', TextColors.WHITE);
    }
}
