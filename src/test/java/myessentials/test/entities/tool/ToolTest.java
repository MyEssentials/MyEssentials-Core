package myessentials.test.entities.tool;

import junit.framework.Assert;
import metest.api.TestPlayer;
import myessentials.entities.api.tool.ToolManager;
import myessentials.test.MECTest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.junit.Before;
import org.junit.Test;

public class ToolTest extends MECTest {

    private EntityPlayerMP player;

    @Before
    public void initTool() {
        player = new TestPlayer(server, "Tool Tester");
        server.worldServerForDimension(0).setBlock(21, 200, 21, Blocks.stone);
    }

    private FakeTool createTool() {
        player.inventory.dropAllItems();
        FakeTool tool = new FakeTool(player);
        // FakePlayer problems
        try {
            // REF: Giving itemStack on registering is not easy to find out
            ToolManager.instance.register(tool);
        } catch (NullPointerException ex) {}
        Assert.assertEquals("The item equipped is not the same as the given item", tool.getItemStack(), player.inventory.getCurrentItem());
        return tool;
    }

    @Test
    public void shouldHandleRightClicks() {
        FakeTool tool = createTool();
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 21, 200, 21, 0, server.worldServerForDimension(0)));
        Assert.assertEquals("Right click event did not get handled by the tool", tool.amountOfClicks, 1);
        try {
            ToolManager.instance.remove(tool);
        } catch (NullPointerException ex) {}
    }

    @Test
    public void shouldHandleShiftRightClicks() {
        player.setSneaking(true);
        FakeTool tool = createTool();
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_AIR, 21, 200, 21, 0, server.worldServerForDimension(0)));
        Assert.assertEquals("Shift right click event did not get handled by the tool", tool.amountOfClicks, 2);
        player.setSneaking(false);
        try {
            ToolManager.instance.remove(tool);
        } catch (NullPointerException ex) {}
    }

    @Test
    public void shouldUpdateDescription() {
        FakeTool tool = createTool();
        Assert.assertEquals("Initial description is wrong", tool.getDescription()[1], "0");
        Assert.assertEquals("Initial description is wrong", tool.getDescription()[2], "1");
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 21, 200, 21, 0, server.worldServerForDimension(0)));
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 21, 200, 21, 0, server.worldServerForDimension(0)));
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, 21, 200, 21, 0, server.worldServerForDimension(0)));
        Assert.assertEquals("Tool did not update the description", tool.getDescription()[1], "3");
        Assert.assertEquals("Tool did not update the description", tool.getDescription()[2], "4");
        try {
            ToolManager.instance.remove(tool);
        } catch (NullPointerException ex) {}
    }
}
