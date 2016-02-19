package myessentials.entities.api.tool;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import myessentials.entities.api.BlockPos;
import myessentials.utils.ChatUtils;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;

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
                t.owner.addChatMessage(new ChatComponentText("You already have a tool!"));
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
    public void onPlayerInteract(PlayerInteractEvent ev) {
        if(!(ev.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR || ev.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack currentStack = ev.entityPlayer.inventory.getCurrentItem();
        if(currentStack == null) {
            return;
        }

        for(Iterator<Tool> it = tools.iterator(); it.hasNext(); ) {
            Tool tool = it.next();
            if(tool.owner == null) {
                it.remove();
                continue;
            }

            if(ev.entityPlayer == tool.owner && tool.getItemStack() == currentStack) {
                if (ev.entityPlayer.isSneaking() && ev.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
                    tool.onShiftRightClick();
                    return;
                } else if (ev.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                    tool.onItemUse(new BlockPos(ev.x, ev.y, ev.z, ev.world.provider.dimensionId), ev.face);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onUseHoe(UseHoeEvent ev) {
        for(Tool tool : tools) {
            if (ev.current == tool.getItemStack()) {
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
