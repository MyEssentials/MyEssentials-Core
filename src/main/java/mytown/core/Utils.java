package mytown.core;

import mytown.core.utils.teleport.EssentialsTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListOps;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.lang.reflect.Method;

/**
 * Created by AfterWind on 12/30/2014.
 * Utils class for a diverse range of methods.
 */
public class Utils {

    /**
     * Returns whether or not a player is OP.
     *
     * @param player
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isOp(EntityPlayer player) {
		if(player == null) // TODO: It appears fakeplayers can be null?
            return false;
        
        if(player.getGameProfile() == null)
            return false; // TODO: Could be for fake players. Still not sure if I should allow it.

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

    /**
     * Teleports player to the dimension without creating any nether portals of sorts.
     * Most of it is code from Delpi (from minecraftforge forums). Thank you!
     */
    public static void teleport(Entity entity, int dim, double x, double y, double z) {
        World world = DimensionManager.getWorld(dim);
        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
        // Offset locations for accuracy
        x = x + 0.5d;
        y = y + 1.0d;
        z = z + 0.5d;
        entity.setPosition(x, y, z);
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
        if (entity.worldObj.provider.dimensionId != dim) {
            playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dim, new EssentialsTeleporter((WorldServer)world));
        }
    }
}
