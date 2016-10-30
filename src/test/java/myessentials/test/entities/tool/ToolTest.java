package myessentials.test.entities.tool;

import org.junit.Assert;
import metest.api.TestPlayer;
import myessentials.entities.api.tool.ToolManager;
import myessentials.test.MECTest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.junit.Before;
import org.junit.Test;

public class ToolTest extends MECTest {

    private EntityPlayerMP player;

    @Before
    public void initTool() {
        player = new TestPlayer(server, "Tool Tester");
        server.worldServerForDimension(0).setBlockState(new BlockPos(21, 200, 21), Blocks.STONE.getDefaultState());
    }

    private FakeTool createTool() {
        player.inventory.dropAllItems();
        FakeTool tool = new FakeTool(player);
        // FakePlayer problems
        try {
            // REF: Method ToolManager.register is calling giveItemStack which should not
            ToolManager.instance.register(tool);
        } catch (NullPointerException ex) {}
        Assert.assertEquals("The item equipped is not the same as the given item", tool.getItemStack(), player.inventory.getCurrentItem());
        return tool;
    }

    @Test
    public void shouldHandleRightClicks() {
        FakeTool tool = createTool();
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, EnumHand.MAIN_HAND, new ItemStack(Items.ARROW), new BlockPos(21, 200, 21), EnumFacing.DOWN, Vec3d.ZERO));
        Assert.assertEquals("Right click event did not get handled by the tool", tool.amountOfClicks, 1);
        try {
            ToolManager.instance.remove(tool);
        } catch (NullPointerException ex) {}
    }

    @Test
    public void shouldHandleShiftRightClicks() {
        player.setSneaking(true);
        FakeTool tool = createTool();
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickEmpty(player, EnumHand.MAIN_HAND));
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
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, EnumHand.MAIN_HAND, new ItemStack(Items.ARROW), new BlockPos(21, 200, 21), EnumFacing.DOWN, Vec3d.ZERO));
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, EnumHand.MAIN_HAND, new ItemStack(Items.ARROW), new BlockPos(21, 200, 21), EnumFacing.DOWN, Vec3d.ZERO));
        MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, EnumHand.MAIN_HAND, new ItemStack(Items.ARROW), new BlockPos(21, 200, 21), EnumFacing.DOWN, Vec3d.ZERO));
        Assert.assertEquals("Tool did not update the description", tool.getDescription()[1], "3");
        Assert.assertEquals("Tool did not update the description", tool.getDescription()[2], "4");
        try {
            ToolManager.instance.remove(tool);
        } catch (NullPointerException ex) {}
    }
}
