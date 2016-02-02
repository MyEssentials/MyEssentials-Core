package myessentials.test.economy;

import metest.api.TestPlayer;
import myessentials.economy.api.Economy;
import myessentials.economy.api.IEconManager;
import myessentials.test.MECTest;
import net.minecraft.entity.player.EntityPlayerMP;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EconomyForgeEssentialsTest extends MECTest {

    private EntityPlayerMP player;
    private Economy economy;
    private IEconManager manager;

    @Before
    public void shouldInitEconomy() {
        player = new TestPlayer(server, "ForgeEssentials Economy Tester");
        economy = new Economy("$ForgeEssentials");
        manager = economy.economyManagerForUUID(player.getPersistentID());
    }

    @Test
    public void shouldGiveMoneyToPlayer() {

        // REF: there should exist a manager.clear() method
        // REF: manager.setWallet() should not take in an EntityPlayer as parameter
        // REF: IEconManager should really be named Wallet
        manager.removeFromWallet(manager.getWallet());
        economy.giveMoneyToPlayer(player, 20);
        Assert.assertEquals("Player did not get enough of the money", manager.getWallet(), 20);
        economy.giveMoneyToPlayer(player, 20);
        Assert.assertEquals("Player did not get enough of the money after calling give method twice", manager.getWallet(), 40);
    }

    @Test
    public void shouldTakeMoneyFromPlayer() {
        manager.removeFromWallet(manager.getWallet());
        manager.addToWallet(50);

        economy.takeMoneyFromPlayer(player, 20);
        Assert.assertEquals("Economy system did not take money", manager.getWallet(), 30);
        economy.takeMoneyFromPlayer(player, 20);
        Assert.assertEquals("Economy system did not take money after calling the take method twice", manager.getWallet(), 10);
    }
}
