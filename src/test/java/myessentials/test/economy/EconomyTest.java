package myessentials.test.economy;

import com.mojang.authlib.GameProfile;
import metest.MinecraftRunner;
import myessentials.economy.Economy;
import myessentials.utils.ItemUtils;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraftforge.common.util.FakePlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(MinecraftRunner.class)
public class EconomyTest {

    public MinecraftServer server;
    Economy economy;
    EntityPlayerMP player;

    @Before
    public void shouldInitEcon() {
        economy = new Economy("minecraft:diamond");
        player = new EntityPlayerMP(server, server.worldServerForDimension(0), new GameProfile(UUID.randomUUID(), "HmmmTasty"), new ItemInWorldManager(server.worldServerForDimension(0)));
        //player = new FakePlayer(server.worldServerForDimension(0), new GameProfile(UUID.randomUUID(), "HmmmTasty"));
    }

    @Test
    public void shouldProvideProperName() {
        Assert.assertEquals("Diamonds", economy.getCurrency(65));
    }

    @Test
    public void shouldGiveItemsFromPlayer() {
        economy.giveMoneyToPlayer(player, 20);
        ItemStack item = PlayerUtils.getItemStackFromPlayer(player, Items.diamond, "Diamond");
        Assert.assertNotNull(player.playerNetServerHandler);
        Assert.assertNotNull(player.inventory);
        Assert.assertEquals(item.stackSize, 20);
    }

}
