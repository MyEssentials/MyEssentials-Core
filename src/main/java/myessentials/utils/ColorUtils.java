package myessentials.utils;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

/**
 * Colors for characters or types of things commonly used.
 */
public class ColorUtils {

    public static final ChatStyle stylePlayer = new ChatStyle().setColor(EnumChatFormatting.WHITE);
    public static final ChatStyle styleOwner = new ChatStyle().setColor(EnumChatFormatting.RED);
    public static final ChatStyle styleTown = new ChatStyle().setColor(EnumChatFormatting.GOLD);
    public static final ChatStyle styleSelectedTown = new ChatStyle().setColor(EnumChatFormatting.GREEN);
    public static final ChatStyle styleInfoText = new ChatStyle().setColor(EnumChatFormatting.GRAY);
    public static final ChatStyle styleConfigurableFlag = new ChatStyle().setColor(EnumChatFormatting.GRAY);
    public static final ChatStyle styleUnconfigurableFlag = new ChatStyle().setColor(EnumChatFormatting.DARK_GRAY);
    public static final ChatStyle styleValueFalse = new ChatStyle().setColor(EnumChatFormatting.RED);
    public static final ChatStyle styleValueRegular = new ChatStyle().setColor(EnumChatFormatting.GREEN);
    public static final ChatStyle styleDescription = new ChatStyle().setColor(EnumChatFormatting.GRAY);
    public static final ChatStyle styleCoords = new ChatStyle().setColor(EnumChatFormatting.BLUE);
    public static final ChatStyle styleComma = new ChatStyle().setColor(EnumChatFormatting.WHITE);
    public static final ChatStyle styleEmpty = new ChatStyle().setColor(EnumChatFormatting.RED);
    public static final ChatStyle styleAdmin = new ChatStyle().setColor(EnumChatFormatting.RED);
    public static final ChatStyle styleGroupType = new ChatStyle().setColor(EnumChatFormatting.BLUE);
    public static final ChatStyle styleGroupText = new ChatStyle().setColor(EnumChatFormatting.GRAY);
    public static final ChatStyle styleGroupParents = new ChatStyle().setColor(EnumChatFormatting.WHITE);
    public static final ChatStyle styleGroup = new ChatStyle().setColor(EnumChatFormatting.BLUE);
    public static final Map<Character, EnumChatFormatting> colorMap = new HashMap<Character, EnumChatFormatting>();
    static {
        ColorUtils.colorMap.put('0', EnumChatFormatting.BLACK);
        ColorUtils.colorMap.put('1', EnumChatFormatting.DARK_BLUE);
        ColorUtils.colorMap.put('2', EnumChatFormatting.DARK_GREEN);
        ColorUtils.colorMap.put('3', EnumChatFormatting.DARK_AQUA);
        ColorUtils.colorMap.put('4', EnumChatFormatting.DARK_RED);
        ColorUtils.colorMap.put('5', EnumChatFormatting.DARK_PURPLE);
        ColorUtils.colorMap.put('6', EnumChatFormatting.GOLD);
        ColorUtils.colorMap.put('7', EnumChatFormatting.GRAY);
        ColorUtils.colorMap.put('8', EnumChatFormatting.DARK_GRAY);
        ColorUtils.colorMap.put('9', EnumChatFormatting.BLUE);
        ColorUtils.colorMap.put('a', EnumChatFormatting.GREEN);
        ColorUtils.colorMap.put('b', EnumChatFormatting.AQUA);
        ColorUtils.colorMap.put('c', EnumChatFormatting.RED);
        ColorUtils.colorMap.put('d', EnumChatFormatting.LIGHT_PURPLE);
        ColorUtils.colorMap.put('e', EnumChatFormatting.YELLOW);
        ColorUtils.colorMap.put('f', EnumChatFormatting.WHITE);
    }

    //public static final String paranthColor = EnumChatFormatting.GOLD.toString();
}
