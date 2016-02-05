package myessentials.test.entities.sign;

import junit.framework.Assert;
import metest.api.TestPlayer;
import myessentials.entities.api.BlockPos;
import myessentials.test.MECTest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.junit.Before;
import org.junit.Test;

public class SignTest extends MECTest {

    private EntityPlayerMP player;

    @Before
    public void init() {
        player = new TestPlayer(server, "Sign Tester");
        server.worldServerForDimension(0).setBlock(20, 199, 20, Blocks.stone);
    }

    @Test
    public void shouldCreateSign() {
        FakeSign sign = new FakeSign(0);
        sign.createSignBlock(player, new BlockPos(20, 200, 20, 0), 0);
        Assert.assertTrue("Sign Block did not get created or did not have proper orientation", server.worldServerForDimension(0).getBlock(20, 200, 20) == Blocks.standing_sign);
        Assert.assertNotNull("Sign TileEntity did not get created", sign.getTileEntity());
        sign.deleteSignBlock();
    }

    @Test
    public void shouldHandleRightClicks() {
        FakeSign sign = new FakeSign(0);
        sign.createSignBlock(player, new BlockPos(20, 200, 20, 0), 0);

        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 20, 200, 20, 1, server.worldServerForDimension(0)));
        Assert.assertEquals("Right click event did not get handled properly", sign.amountOfClicks, 1);
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 20, 200, 20, 1, server.worldServerForDimension(0)));
        Assert.assertEquals("Right click event did not get handled properly after posting twice", sign.amountOfClicks, 2);

        sign.deleteSignBlock();
    }

    @Test
    public void shouldHandleShiftRightClicks() {
        FakeSign sign = new FakeSign(0);
        sign.createSignBlock(player, new BlockPos(20, 200, 20, 0), 0);

        player.setSneaking(true);

        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 20, 200, 20, 1, server.worldServerForDimension(0)));
        Assert.assertEquals("Shift right click event did not get handled properly", sign.amountOfClicks, 2);
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 20, 200, 20, 1, server.worldServerForDimension(0)));
        Assert.assertEquals("Shift right click event did not get handled properly", sign.amountOfClicks, 4);

        sign.deleteSignBlock();
    }

}
