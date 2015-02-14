package mytown.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserList;
import net.minecraft.server.management.UserListOps;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

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
    public static boolean isOp(EntityPlayer player) {
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
                MyTownCore.Instance.log.info(mt.getName());
            }
            e.printStackTrace();
        }
        return false;
    }
}
