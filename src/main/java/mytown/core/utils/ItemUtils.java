package mytown.core.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * All utilities that are exclusively for Items and ItemStacks go here.
 */
public class ItemUtils {

    private ItemUtils() {

    }

    /**
     * Returns the item from a String that has this pattern: (modid):(unique_name)[:meta]
     */
    public static Item itemFromName(String itemName) {
        String[] split = itemName.split(":");
        return GameRegistry.findItem(split[0], split[1]);
    }

    /**
     * Returns the ItemStack from a String that has this pattern: (modid):(unique_name)[:meta]
     */
    public static ItemStack itemStackFromName(String itemName) {
        String[] split = itemName.split(":");

        return new ItemStack(GameRegistry.findItem(split[0], split[1]), 1, split.length > 2 ? Integer.parseInt(split[2]) : 0);
    }

    /**
     * Returns the unique identifier of given ItemStack
     */
    public static String nameFromItemStack(ItemStack itemStack) {
        String name = GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).toString();
        if(itemStack.getItemDamage() != 0)
            name += ":" + itemStack.getItemDamage();
        return name;
    }
}
