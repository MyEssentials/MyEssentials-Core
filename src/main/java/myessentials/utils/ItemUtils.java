package myessentials.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
    }

    /**
     * Returns the ItemStack from a String that has this pattern: (modid):(unique_name)[:meta]
     */
    public static ItemStack itemStackFromName(String itemName) {
        String[] split = itemName.split(":");

        Item item = Item.REGISTRY.getObject(new ResourceLocation(split[0] + ":" + split[1]));
        if (item == null) {
            return null;
        }

        return new ItemStack(item, 1, split.length > 2 ? Integer.parseInt(split[2]) : 0);
    }

    /**
     * Returns the unique identifier of given ItemStack
     */
    public static String nameFromItemStack(ItemStack itemStack) {
        String name = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString();
        if(itemStack.getItemDamage() != 0) {
            name += ":" + itemStack.getItemDamage();
        }
        return name;
    }
}
