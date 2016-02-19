package myessentials.test.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import junit.framework.Assert;
import myessentials.test.MECTest;
import myessentials.utils.ItemUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.Test;

public class ItemUtilsTest extends MECTest {

    @Test
    public void shouldGetItemFromName() {

        Assert.assertEquals("Item should exist in the current registry", Items.bow, ItemUtils.itemFromName("minecraft:bow"));
        Assert.assertNull("Method should have returned null since item does not exist in the current registry", ItemUtils.itemFromName("invalid:kek"));

    }

    @Test
    public void shouldGetItemStackFromName() {

        ItemStack stack = ItemUtils.itemStackFromName("minecraft:dye:1");
        Assert.assertEquals("ItemStack should be found in the current registry", Items.dye, stack.getItem());
        Assert.assertEquals("ItemStack should have been created with the given meta", 1, stack.getItemDamage());
        Assert.assertEquals("ItemStack should have only 1 of given Item", 1, stack.stackSize);
        Assert.assertNull("Method should have returned null since the Item given does not exist in the current registry", ItemUtils.itemStackFromName("invalid:kek:12"));

    }

    @Test
    public void shouldGetUniqueIdentifierFromStack() {

        ItemStack stack = new ItemStack(Items.blaze_rod, 2, 15);
        Assert.assertEquals("The unique identifier did not get created correctly", "minecraft:blaze_rod:15", ItemUtils.nameFromItemStack(stack));
        stack = new ItemStack(Blocks.stone, 15);
        Assert.assertEquals("The unique identifier did not get created correctly", "minecraft:stone", ItemUtils.nameFromItemStack(stack));

    }

}
