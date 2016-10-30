package myessentials.utils;

import com.mojang.authlib.GameProfile;
import myessentials.MyEssentialsCore;
import myessentials.entities.api.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.UserListOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * All utilities that are exclusively for EntityPlayerMP or EntityPlayer go here.
 */
public class PlayerUtils {

    private PlayerUtils() { }

    /**
     * Takes the amount of items specified.
     * Returns false if player doesn't have the items necessary
     */
    public static boolean takeItemFromPlayer(EntityPlayer player, String itemName, int amount) {
        String[] split = itemName.split(":");
        return takeItemFromPlayer(player, ItemUtils.itemFromName(split[0] + ":" + split[1]), amount, split.length == 3 ? Integer.parseInt(split[2]) : -1);
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
                if (itemSum >= amount)
                    break;
            }
        }

        if (itemSum < amount)
            return false;

        for (int i : slots) {
            if (player.inventory.mainInventory[i].stackSize >= amount) {
                player.inventory.decrStackSize(i, amount);
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);

                ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
                return true;
            } else {
                int stackSize = player.inventory.mainInventory[i].stackSize;
                player.inventory.decrStackSize(i, stackSize);
                amount -= stackSize;
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
            }
        }
        return true;
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, String itemName, int amount) {
        String[] split = itemName.split(":");
        giveItemToPlayer(player, ItemUtils.itemFromName(split[0] + ":" + split[1]), amount, split.length > 2 ? Integer.parseInt(split[2]) : 0);
    }

    /**
     * Gives the amount of items specified.
     */
    public static void giveItemToPlayer(EntityPlayer player, Item item, int amount, int meta) {
        int left = amount;
        // While there are items left...
        while (left > 0) {
            int i = -1;
            // Find a matching ItemStacks that can be added to
            for (int j = 0; j < player.inventory.mainInventory.length; j++) {
                if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == item
                        && player.inventory.mainInventory[j].getItemDamage() == meta
                        && player.inventory.mainInventory[j].stackSize < 64) {
                    i = j;
                    break;
                }
            }
            if (i == -1) {
                // No matching ItemStacks that can be filled, look for empty slots
                for (int j = 0; j < player.inventory.mainInventory.length; j++) {
                    if (player.inventory.mainInventory[j] == null) {
                        i = j;
                        break;
                    }
                }
                if (i != -1) {
                    if (left > 64) {
                        player.inventory.mainInventory[i] = new ItemStack(item, 64, meta);
                        left -= 64;
                    } else {
                        player.inventory.mainInventory[i] = new ItemStack(item, left, meta);
                        left = 0;
                    }
                }
            } else {
                // Add what we can can to fill the ItemStack
                if (player.inventory.mainInventory[i].stackSize + left > 64) {
                    left -= (64 - player.inventory.mainInventory[i].stackSize);
                    player.inventory.mainInventory[i].stackSize = 64;
                } else {
                    player.inventory.mainInventory[i].stackSize += left;
                    left = 0;
                }
            }

            if (i == -1) {
                // Drop it on the ground if it fails to add to the inventory
                ItemStack itemStack;
                if (left > 64) {
                    itemStack = new ItemStack(item, 64, meta);
                    left -= 64;
                } else {
                    itemStack = new ItemStack(item, left, meta);
                    left = 0;
                }
                WorldUtils.dropAsEntity(player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ, itemStack);
            } else {
                // get the actual inventory Slot:
                Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
                // send S2FPacketSetSlot to the player with the new / changed stack (or null)
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
            }
        }
    }

    /**
     * Gives the player an ItemStack
     */
    public static void giveItemStackToPlayer(EntityPlayer player, ItemStack itemStack) {
        int i = -1;
        for (int j = 0; j < player.inventory.mainInventory.length; j++) {
            if (player.inventory.mainInventory[j] == null) {
                i = j;
                break;
            }
        }
        if (i != -1) {
            player.inventory.mainInventory[i] = itemStack;
            // get the actual inventory Slot:
            Slot slot = player.openContainer.getSlotFromInventory(player.inventory, i);
            // send S2FPacketSetSlot to the player with the new / changed stack (or null)
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, player.inventory.mainInventory[i]));
        } else {
            WorldUtils.dropAsEntity(player.getEntityWorld(), (int) player.posX, (int) player.posY, (int) player.posZ, itemStack);
        }
    }

    /**
     * Gets the first occurrence of the item from a player's inventory.
     */
    public static ItemStack getItemStackFromPlayer(EntityPlayer player, Item item, String name) {
        for (int i = 0; i < player.inventory.mainInventory.length; i++) {
            ItemStack itemStack = player.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() == item && itemStack.getDisplayName().equals(name)) {
                return itemStack;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void teleportEntity(Entity entity, Position pos) {
        if (entity == null || entity.worldObj.isRemote || entity.isBeingRidden()) return;

        World startWorld = entity.worldObj;
        World destinationWorld = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(pos.dim());

        Entity mount = entity.getRidingEntity();
        if (mount != null && mount != entity) {
            entity.dismountRidingEntity();
            teleportEntity(mount, pos);
        }

        boolean interDimensional = startWorld.provider.getDimension() != destinationWorld.provider.getDimension();

        startWorld.updateEntityWithOptionalForce(entity, false);//added

        if ((entity instanceof EntityPlayerMP) && interDimensional) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.closeScreen();//added
            player.dimension = pos.dim();
            player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), destinationWorld.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));
            startWorld.removeEntityDangerously(player);

            startWorld.playerEntities.remove(player);
            startWorld.updateAllPlayersSleepingFlag();
            int i = entity.chunkCoordX;
            int j = entity.chunkCoordZ;
            if ((entity.addedToChunk) && (startWorld.getChunkFromBlockCoords(new BlockPos(entity.posX, entity.posY, entity.posZ)).isPopulated())) //todo make sure this isnt broken			{
            {
                startWorld.getChunkFromChunkCoords(i, j).removeEntity(entity);
                startWorld.getChunkFromChunkCoords(i, j).setModified(true);
            }
            startWorld.loadedEntityList.remove(entity);
            startWorld.onEntityRemoved(entity);
        }

        entity.setLocationAndAngles(pos.x(), pos.y(), pos.z(), 0, 0);

        ((WorldServer) destinationWorld).getChunkProvider().loadChunk(pos.xi() >> 4, pos.zi() >> 4);

        destinationWorld.theProfiler.startSection("placing");
        if (interDimensional) {
            if (!(entity instanceof EntityPlayer)) {
                NBTTagCompound entityNBT = new NBTTagCompound();
                entity.isDead = false;
                entityNBT.setString("id", EntityList.getEntityString(entity));
                entity.writeToNBT(entityNBT);
                entity.isDead = true;
                entity = EntityList.createEntityFromNBT(entityNBT, destinationWorld);
                if (entity == null) {
                    MyEssentialsCore.instance.LOG.error("Failed to teleport entity to new location");
                    return;
                }
                entity.dimension = destinationWorld.provider.getDimension();
            }
            destinationWorld.spawnEntityInWorld(entity);
            entity.setWorld(destinationWorld);
        }
        entity.setLocationAndAngles(pos.x(), pos.y(), pos.z(), 0, 0);

        destinationWorld.updateEntityWithOptionalForce(entity, false);
        entity.setLocationAndAngles(pos.x(), pos.y(), pos.z(), 0, 0);

        if ((entity instanceof EntityPlayerMP)) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (interDimensional) {
                player.mcServer.getPlayerList().preparePlayer(player, (WorldServer) destinationWorld);
            }
            player.connection.setPlayerLocation(pos.x(), pos.y(), pos.z(), player.rotationYaw, player.rotationPitch);
        }

        destinationWorld.updateEntityWithOptionalForce(entity, false);

        if (((entity instanceof EntityPlayerMP)) && interDimensional) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.interactionManager.setWorld((WorldServer) destinationWorld);
            player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) destinationWorld);
            player.mcServer.getPlayerList().syncPlayerInventory(player);

            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), potionEffect));
            }

            player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, startWorld.provider.getDimension(), destinationWorld.provider.getDimension());
        }
        entity.setLocationAndAngles(pos.x(), pos.y(), pos.z(), 0, entity.rotationPitch);

        if (mount != null) {

            entity.startRiding(mount);
            if ((entity instanceof EntityPlayerMP)) {
                destinationWorld.updateEntityWithOptionalForce(entity, true);
            }
        }
        destinationWorld.theProfiler.endSection();
        entity.fallDistance = 0;
    }

    /**
     * Teleports a player to (x, y, z) in dimension dim without creating any nether portals of sorts.
     * Also preserves motion and potion effects even on cross dimensional teleports.
     * Only the player teleports, leaving mounts behind.
     * <p/>
     * The base of the Teleport code came from CoFHLib teleport code:
     * https://github.com/CoFH/CoFHLib/blob/master/src/main/java/cofh/lib/util/helpers/EntityHelper.java
     */
    /*public static void teleport(EntityPlayerMP player, int dim, double x, double y, double z) {
        // if (player.isBeingRidden()) { }

        if (player.isRiding()) {
            player.dismountRidingEntity();
        }
        if (player.dimension != dim) {
            transferPlayerToDimension(player, dim);
        }
        player.setPositionAndUpdate(x, y, z);
    }
*/
    /**
     * Transfers a player to a new dimension preserving potion effects and motion.
     */
  /*  public static void transferPlayerToDimension(EntityPlayerMP player, int dim) {
        ServerConfigurationManager configManager = player.mcServer.getConfigurationManager();
        int oldDimension = player.dimension;

        WorldServer oldWorldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(oldDimension);
        WorldServer newWorldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(dim);

        player.closeScreen();
        player.dimension = dim;
        player.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        oldWorldServer.removeEntityDangerously(player);
        oldWorldServer.playerEntities.remove(player);
        oldWorldServer.updateAllPlayersSleepingFlag();

        player.isDead = false;

        transferPlayerToWorld(player, oldWorldServer, newWorldServer);
        configManager.func_72375_a(player, oldWorldServer);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(newWorldServer);
        configManager.updateTimeAndWeatherForPlayer(player, newWorldServer);
        configManager.syncPlayerInventory(player);
        for (PotionEffect potioneffect : player.getActivePotionEffects()) {
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDimension, dim);
    }
*/
    /**
     * Tranfers a player to a new world preserving motion.
     */
  /*  public static void transferPlayerToWorld(EntityPlayerMP player, WorldServer oldWorld, WorldServer newWorld) {
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
    }*/

    public static GameProfile getGameProfile(UUID uuid) {
        for (GameProfile profile : FMLCommonHandler.instance().getMinecraftServerInstance().getGameProfiles()) {
            if (profile.getId() == uuid) {
                return profile;
            }
        }
        return null;
    }

    public static GameProfile getGameProfile(String username) {
        for (GameProfile profile : FMLCommonHandler.instance().getMinecraftServerInstance().getGameProfiles()) {
            if (profile.getName().equals(username)) {
                return profile;
            }
        }
        return null;
    }

    public static boolean isOp(EntityPlayer player) {
        return isOp(player.getGameProfile());
    }

    public static boolean isOp(UUID uuid) {
        return isOp(getGameProfile(uuid));
    }

    public static boolean isOp(GameProfile gameProfile) {
        UserListOps ops = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers();
        for (String name : ops.getKeys()) {
            if (gameProfile.getName().equals(name)) {
                return true;
            }
        }
        return false;
        /*
        try {
            Class clazz = Class.forName("net.minecraft.server.management.UserList");
            Method method = clazz.getDeclaredMethod("func_152692_d", Object.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(ops, gameProfile);
        } catch (Exception e) {
            for (Method mt : UserList.class.getMethods()) {
                MyEssentialsCore.instance.LOG.info(mt.getName());
            }
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(e));
        }

        return false;
        */
    }


    @SuppressWarnings("unchecked")
    public static EntityPlayer getPlayer(UUID uuid) {
        for(EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList()) {
            if(player.getGameProfile().getId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public static UUID getUUID(String username) {
        GameProfile profile = getGameProfile(username);
        return profile == null ? null : profile.getId();
    }

    public static String getUsername(UUID uuid) {
        GameProfile profile = getGameProfile(uuid);
        return profile == null ? null : profile.getName();
    }

    public static List<String> getAllUsernames() {
        return Arrays.asList(FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
    }
}