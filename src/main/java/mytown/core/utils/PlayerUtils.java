package mytown.core.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mytown.core.MyEssentialsCore;
import mytown.core.utils.teleport.EssentialsTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Joe Goett
 * All utilities that are exclusively for EntityPlayerMP or EntityPlayer go here.
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

    /**
     * Teleports player to the dimension without creating any nether portals of sorts.
     * Most of it is code from Delpi (from minecraftforge forums). Thank you!
     */
    public static void teleport(EntityPlayerMP player, int dim, double x, double y, double z) {

        // Offset locations for accuracy
        x = x + 0.5d;
        y = y + 1.0d;
        z = z + 0.5d;
        player.motionX = player.motionY = player.motionZ = 0.0D;
        player.setPosition(x, y, z);
        if (player.worldObj.provider.dimensionId != dim) {
            transferPlayerToDimension(player, dim, x, y, z);
            //World world = DimensionManager.getWorld(dim);
            //player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dim, new EssentialsTeleporter((WorldServer)world));
        }
    }

    /**
     * krwminer's interdimensional teleport code.
     */
    public static void transferPlayerToDimension(EntityPlayerMP player, int dim, double x, double y, double z) {
        ServerConfigurationManager configManager = player.mcServer.getConfigurationManager();
        int oldDimension = player.dimension;

        WorldServer oldWorldServer = configManager.getServerInstance().worldServerForDimension(oldDimension);
        WorldServer newWorldServer = configManager.getServerInstance().worldServerForDimension(dim);

        player.dimension = dim;
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        oldWorldServer.removePlayerEntityDangerously(player);
        player.isDead = false;

        transferPlayerToWorld(player, oldWorldServer, newWorldServer);
        configManager.func_72375_a(player, oldWorldServer);

        player.setLocationAndAngles(x + 0.5D, y + 0.1D, z + 0.5D, 0.0F, 0.0F);

        newWorldServer.theChunkProviderServer.loadChunk((int)player.posX >> 4, (int)player.posZ >> 4);
        while (!newWorldServer.getCollidingBoundingBoxes(player, player.boundingBox).isEmpty()) {
            player.setPosition(player.posX, player.posY + 1.0D, player.posZ);
        }

        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(newWorldServer);
        configManager.updateTimeAndWeatherForPlayer(player, newWorldServer);
        configManager.syncPlayerInventory(player);
        Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
        while (iterator.hasNext()) {
            PotionEffect potioneffect = iterator.next();
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDimension, dim);
    }

    /**
     * krwminer's interdimensional teleport code.
     */
    public static void transferPlayerToWorld(EntityPlayerMP player, WorldServer oldWorld, WorldServer newWorld) {
        double moveFactor = oldWorld.provider.getMovementFactor() / newWorld.provider.getMovementFactor();
        double x = player.posX * moveFactor;
        double z = player.posZ * moveFactor;
        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);
        if (player.isEntityAlive()) {
            player.setLocationAndAngles(x, player.posY, z, player.rotationYaw, player.rotationPitch);
            newWorld.spawnEntityInWorld(player);
            newWorld.updateEntityWithOptionalForce(player, false);
        }
        player.setWorld(newWorld);
    }


    /**
     * Returns whether or not a player is OP.
     */
    @SuppressWarnings("unchecked")
    public static boolean isOp(EntityPlayer player) {
		if(player == null)
            return false;

        if(player.getGameProfile() == null)
            return false;

        UserListOps ops = MinecraftServer.getServer().getConfigurationManager().func_152603_m();
        try {
            Class clazz = Class.forName("net.minecraft.server.management.UserList");
            Method method = clazz.getDeclaredMethod("func_152692_d", Object.class);
            method.setAccessible(true);
            return (Boolean)method.invoke(ops, player.getGameProfile());
        } catch (Exception e) {
            for(Method mt : UserList.class.getMethods()) {
                MyEssentialsCore.Instance.log.info(mt.getName());
            }
            e.printStackTrace();
        }
        return false;
    }
}
