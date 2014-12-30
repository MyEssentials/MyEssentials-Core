package mytown.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

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
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile());
    }
}
