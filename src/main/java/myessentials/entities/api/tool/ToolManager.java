package myessentials.entities.api.tool;

import myessentials.entities.api.Position;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * If a tool is created, registering it here will route all the needed events to it.
 */
public class ToolManager {

    public static final ToolManager instance = new ToolManager();

    private final List<Tool> tools = new ArrayList<Tool>();

    public boolean register(Tool tool) {
        for(Tool t : tools) {
            if(t.owner == tool.owner && t.getItemStack() != null) {
                t.owner.addChatMessage(new TextComponentString("You already have a tool!"));
                return false;
            }
        }

        tools.add(tool);
        tool.giveItemStack();
        return true;
    }

    public Tool get(EntityPlayer owner) {
        for(Tool tool : tools) {
            if(tool.owner == owner) {
                return tool;
            }
        }
        return null;
    }

    public void remove(Tool tool) {
        tools.remove(tool);
        PlayerUtils.takeItemFromPlayer(tool.owner, tool.getItemStack(), 1);
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock ev) {
        ItemStack currentStack = ev.getEntityPlayer().inventory.getCurrentItem();
        if(currentStack == null) {
            return;
        }

        for(Iterator<Tool> it = tools.iterator(); it.hasNext(); ) {
            Tool tool = it.next();
            if(tool.owner == null) {
                it.remove();
                continue;
            }

            if(ev.getEntityPlayer() == tool.owner && tool.getItemStack() == currentStack) {
                tool.onItemUse(new Position(ev.getPos(), ev.getWorld().provider.getDimension()), ev.getFace());
                return;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem ev) {
        ItemStack currentStack = ev.getEntityPlayer().inventory.getCurrentItem();
        if(currentStack == null) {
            return;
        }

        for(Iterator<Tool> it = tools.iterator(); it.hasNext(); ) {
            Tool tool = it.next();
            if(tool.owner == null) {
                it.remove();
                continue;
            }

            if(ev.getEntityPlayer() == tool.owner && tool.getItemStack() == currentStack) {
                if (ev.getEntityPlayer().isSneaking()) {
                    tool.onShiftRightClick();
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onUseHoe(UseHoeEvent ev) {
        for(Tool tool : tools) {
            if (ev.getCurrent().equals(tool.getItemStack())) {
                ev.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent ev) {
        for(int i = 0; i < ev.player.inventory.getSizeInventory(); i++) {
            ItemStack stack = ev.player.inventory.getStackInSlot(i);
            if(stack == null || !stack.getDisplayName().startsWith(Tool.IDENTIFIER)) {
                continue;
            }

            PlayerUtils.takeItemFromPlayer(ev.player, stack, 1);
        }
    }
}
