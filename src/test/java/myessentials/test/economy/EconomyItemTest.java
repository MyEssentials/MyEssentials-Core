package myessentials.test.economy;

import metest.api.TestPlayer;
import myessentials.economy.api.Economy;
import myessentials.test.MECTest;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//@RunWith(MinecraftRunner.class)
public class EconomyItemTest extends MECTest {

    private Economy economy;
    private EntityPlayerMP player;

    @Before
    public void shouldInitEcon() {
        economy = new Economy("minecraft:diamond");
        player = new TestPlayer(server, "Economy Tester");
    }

    @Test
    public void shouldGiveItemsFromPlayer() {
        player.inventory.dropAllItems();
        // Catching NPE because NetPlayerHandler is null for FakePlayers
        try {
            economy.giveMoneyToPlayer(player, 20);
        } catch (NullPointerException ex) {}

        // REF: get item stack through economy instead of the PlayerUtils
        ItemStack item = PlayerUtils.getItemStackFromPlayer(player, Items.diamond, "Diamond");
        Assert.assertNotNull("Player did not get any of the cost item!", item);
        Assert.assertEquals("Player did not get enough of the cost item!", item.stackSize, 20);
    }

    @Test
    public void shouldTakeItemsFromPlayer() {
        player.inventory.dropAllItems();
        try {
            economy.giveMoneyToPlayer(player, 20);
        } catch (NullPointerException ex) {}

        try {
            economy.takeMoneyFromPlayer(player, 20);
        } catch (NullPointerException ex) {}

        ItemStack item = PlayerUtils.getItemStackFromPlayer(player, Items.diamond, "Diamond");
        Assert.assertNull("Economy system did not take all of the cost item!", item);
    }

    @Test
    public void shouldGetProperCurrencyString() {
        // REF: cost item name should be added to the Economy class
        Assert.assertEquals("Did not get the proper currency String!", economy.getCurrency(30), "Diamonds");
    }
}
