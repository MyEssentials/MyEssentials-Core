package mytown.core.utils;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S2FPacketSetSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Joe Goett
 */
public class PlayerUtils {
    /**
     * Takes the amount of items specified.
     * Returns false if player doesn't have the items necessary
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, String itemName, int amount) {
        String[] split = itemName.split(":");
        return takeItemFromPlayer(player, GameRegistry.findItem(split[0], split[1]), amount, split.length == 3 ? Integer.parseInt(split[2]) : -1);
    }

    /**
     * Takes a specified amount of the itemStack from the player's inventory.
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, ItemStack itemStack, int amount) {
        return takeItemFromPlayer(player, itemStack.getItem(), amount, itemStack.getItemDamage());
    }

    /**
     * Takes the amount of items specified.
     * Returns false if player doesn't have the items necessary
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, Item item, int amount, int meta) {
        List<Integer> slots = new ArrayList<Integer>();
        int itemSum = 0;
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack itemStack = player.inventory.mainInventory[i];
            if (itemStack == null)
                continue;
            if (itemStack.getItem() == item && (meta == -1 || itemStack.getItemDamage() == meta)) {
                slots.add(i);
                itemSum += itemStack.stackSize;
                if(itemSum >= amount)
                    break;
            }
        }

        if(itemSum < amount)
            return false;

        for(int i : slots) {
            if(player.inventory.mainInventory[i].stackSize >= amount) {
                player.inventory.decrStackSize(i, amount);
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
                return true;
            } else {
                int stackSize = player.inventory.mainInventory[i].stackSize;
                player.inventory.decrStackSize(i, stackSize);
                amount -= stackSize;
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
            }
        }
        return true;
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, String itemName, int amount) {
        String[] split = itemName.split(":");
        giveItemToPlayer(player, GameRegistry.findItem(split[0], split[1]), amount, split.length > 2 ? Integer.parseInt(split[2]) : 0);
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, ItemStack itemStack, int amount) {
        giveItemToPlayer(player, itemStack.getItem(), amount, itemStack.getItemDamage());
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, Item item, int amount, int meta) {
        for (int left = amount; left > 0; left -= 64) {
            ItemStack stack = new ItemStack(item, left > 64 ? 64 : left, meta);
            //stack = addToInventory(player.inventory, stack);
            int i = -1;
            for(int j = 0; j < player.inventory.mainInventory.length; j++) {
                if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == item && player.inventory.mainInventory[j].getItemDamage() == meta &&
                        player.inventory.mainInventory[j].stackSize + stack.stackSize <= 64) {
                    i = j;
                    break;
                }
            }
            if(i == -1) {
                for(int j = 0; j < player.inventory.mainInventory.length; j++) {
                    if(player.inventory.mainInventory[j] == null) {
                        i = j;
                        break;
                    }
                }
                if(i != -1)
                    player.inventory.mainInventory[i] = stack;
            } else {
                player.inventory.mainInventory[i].stackSize += amount;
            }

            if (i == -1) {
                // Drop it on the ground if it fails to add to the inventory
                EntityUtils.dropAsEntity(player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ, stack);
            } else {

                // get the actual inventory Slot:
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                // send S2FPacketSetSlot to the player with the new / changed stack (or null)
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
            }
        }
    }
}
