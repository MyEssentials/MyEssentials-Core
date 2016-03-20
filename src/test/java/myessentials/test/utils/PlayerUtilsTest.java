package myessentials.test.utils;

import com.mojang.authlib.GameProfile;
import junit.framework.Assert;
import myessentials.test.MECTest;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class PlayerUtilsTest extends MECTest {

    private EntityPlayerMP player;

    @Before
    public void shouldInitPlayer() {
        player = new FakePlayer(server.worldServerForDimension(0), new GameProfile(UUID.randomUUID(), "UtilsTester"));
    }

    @Test
    public void shouldTakeItemsFromPlayer() {

        player.inventory.setInventorySlotContents(10, new ItemStack(Items.gold_ingot, 64));
        // REF: Refactor the take item method to be easier to understand
        try {
            PlayerUtils.takeItemFromPlayer(player, "minecraft:gold_ingot", 50);
        } catch (NullPointerException ex) {}
        ItemStack stack = player.inventory.getStackInSlot(10);
        Assert.assertEquals("Player should have less items", 14, stack.stackSize);

        try {
            PlayerUtils.takeItemFromPlayer(player, "minecraft:gold_ingot", 14);
        } catch (NullPointerException ex) {}
        stack = player.inventory.getStackInSlot(10);
        Assert.assertNull("Method should have taken all the items from the player", stack);

    }

    @Test
    public void shouldGiveItemsToPlayer() {

        // REF: Refactor the give item method to be easier to understand
        // REF: Make the give/take item methods handle the NPE themselves
        try {
            PlayerUtils.giveItemToPlayer(player, "minecraft:gold_ingot", 64);
        } catch (NullPointerException ex) {}

        ItemStack stack = player.inventory.getStackInSlot(0);

        Assert.assertNotNull("Player should have got the item in its inventory", stack);
        Assert.assertEquals("Player should have got the proper amount of items", 64, stack.stackSize);

    }

    @Test
    public void shouldNotGiveItemsToPlayerThatHasAFullInventory() {

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            player.inventory.setInventorySlotContents(i, new ItemStack(Items.stone_axe, 1));
        }

        try {
            PlayerUtils.giveItemToPlayer(player, "minecraft:gold_ingot", 4);
        } catch (NullPointerException ex) {}

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            Assert.assertFalse("The give method should not have modified the inventory's contents. It should have thrown the items on the ground instead", player.inventory.getStackInSlot(i).getItem() == Items.gold_ingot);
        }

    }

    @Test
    public void shouldGiveItemStackToPlayer() {

        try {
            PlayerUtils.giveItemStackToPlayer(player, new ItemStack(Items.gold_ingot, 50));
        } catch (NullPointerException ex) {}

        ItemStack stack = player.inventory.getStackInSlot(0);
        Assert.assertNotNull("Player should have got the ItemStack in its inventory", stack);
        Assert.assertEquals("Player did not get the expected amount of items in the ItemStack", 50, stack.stackSize);

    }

    @Test
    public void shouldGetItemStackFromPlayer() {

        player.inventory.setInventorySlotContents(15, new ItemStack(Items.gold_ingot, 30));
        player.inventory.setInventorySlotContents(14, new ItemStack(Items.stone_axe, 1));

        ItemStack stack = PlayerUtils.getItemStackFromPlayer(player, Items.gold_ingot, "Gold Ingot");
        Assert.assertNotNull("Method did not get any ItemStack", stack);
        Assert.assertEquals("Method did not get the proper ItemStack", Items.gold_ingot, stack.getItem());
        Assert.assertEquals("Method did not get the proper ItemStack", 30, stack.stackSize);

    }


    // REF: This will be tested in its own class once we move the new teleport functionality
    //@Test
    public void shouldTeleportPlayer() {
    }

    @Test
    public void shouldVerifyIfPlayerIsOP() {

        Assert.assertFalse("Player is not OP but the method did not detect that", PlayerUtils.isOp(player));

    }

    @After
    public void tearDown() {
        player.inventory.dropAllItems();
    }
}
