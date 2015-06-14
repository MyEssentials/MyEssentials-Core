package mytown.core.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mytown.core.MyEssentialsCore;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * All utilities that are exclusively for EntityPlayerMP or EntityPlayer go here.
 */
public class PlayerUtils {

    private PlayerUtils() {

    }

    /**
     * Takes the amount of items specified.
     * Returns false if player doesn't have the items necessary
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, String itemName, int amount) {
        String[] split = itemName.split(":");
        return takeItemFromPlayer(player, GameRegistry.findItem(split[0], split[1]), amount, split.length == 3 ? Integer.parseInt(split[2]) : -1);
    }

    /**
     * Takes the amount of items specified.
     * Returns false if player doesn't have the items necessary
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, Item item, int amount, int meta) {
        return takeItemFromPlayer(player, new ItemStack(item, 1, meta), amount);
    }

    /**
     * Takes a specified amount of the itemStack from the player's inventory.
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, ItemStack itemStack, int amount) {
        List<Integer> slots = new ArrayList<Integer>();
        int itemSum = 0;
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack invStack = player.inventory.mainInventory[i];
            if (invStack == null)
                continue;
            if (invStack.getItem() == itemStack.getItem() && invStack.getDisplayName().equals(itemStack.getDisplayName()) && invStack.getItemDamage() == itemStack.getItemDamage()) {
                slots.add(i);
                itemSum += invStack.stackSize;
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
    public static void giveItemToPlayer(EntityPlayer player, Item item, int amount, int meta) {
        giveItemToPlayer(player, new ItemStack(item, 1, meta), amount);
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, ItemStack itemStack, int amount) {
        for (int left = amount; left > 0; left -= 64) {
            int i = -1;
            for(int j = 0; j < player.inventory.mainInventory.length; j++) {
                if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == itemStack.getItem()
                        && player.inventory.mainInventory[j].getItemDamage() == itemStack.getItemDamage()
                        && player.inventory.mainInventory[j].stackSize + itemStack.stackSize <= 64) {
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
                    player.inventory.mainInventory[i] = itemStack;
            } else {
                player.inventory.mainInventory[i].stackSize += amount;
            }

            if (i == -1) {
                // Drop it on the ground if it fails to add to the inventory
                WorldUtils.dropAsEntity(player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ, itemStack);
            } else {
                // get the actual inventory Slot:
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                // send S2FPacketSetSlot to the player with the new / changed stack (or null)
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
            }
        }
    }


    /**
     * Gets the first occurrence of the item from a player's inventory.
     */
    public static ItemStack getItemStackFromPlayer(EntityPlayer player, Item item, String name) {
        for(int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack itemStack = player.inventory.mainInventory[i];
            if(itemStack != null && itemStack.getItem() == item && itemStack.getDisplayName().equals(name)) {
                return itemStack;
            }
        }
        return null;
    }

    /**
     * Teleports player to the dimension without creating any nether portals of sorts.
     */
    public static void teleport(EntityPlayerMP player, int dim, double x, double y, double z) {
        player.motionX = player.motionY = player.motionZ = 0.0D;
        player.setPositionAndUpdate(x, y, z);
        if (player.worldObj.provider.dimensionId != dim) {
            transferPlayerToDimension(player, dim, x, y, z);
            //World world = DimensionManager.getWorld(dim);
            //player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dim, new EssentialsTeleporter((WorldServer)world));
        }
    }

    /**
     * krwminer's interdimensional teleport code.
     */
    @SuppressWarnings("unchecked")
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
        for (PotionEffect potioneffect : (Iterable<PotionEffect>) player.getActivePotionEffects()) {
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
                MyEssentialsCore.instance.LOG.info(mt.getName());
            }
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    /**
     * Gets the position at which the player is looking
     */
    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World p_77621_1_, EntityPlayer p_77621_2_, boolean p_77621_3_) {
        float f = 1.0F;
        float f1 = p_77621_2_.prevRotationPitch + (p_77621_2_.rotationPitch - p_77621_2_.prevRotationPitch) * f;
        float f2 = p_77621_2_.prevRotationYaw + (p_77621_2_.rotationYaw - p_77621_2_.prevRotationYaw) * f;
        double d0 = p_77621_2_.prevPosX + (p_77621_2_.posX - p_77621_2_.prevPosX) * (double) f;
        double d1 = p_77621_2_.prevPosY + (p_77621_2_.posY - p_77621_2_.prevPosY) * (double) f + (double) (p_77621_1_.isRemote ? p_77621_2_.getEyeHeight() - p_77621_2_.getDefaultEyeHeight() : p_77621_2_.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
        double d2 = p_77621_2_.prevPosZ + (p_77621_2_.posZ - p_77621_2_.prevPosZ) * (double) f;
        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        if (p_77621_2_ instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) p_77621_2_).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
        return p_77621_1_.func_147447_a(vec3, vec31, p_77621_3_, !p_77621_3_, false);
    }
}