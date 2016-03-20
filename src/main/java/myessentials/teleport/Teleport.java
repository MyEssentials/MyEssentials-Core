package myessentials.teleport;

import myessentials.entities.api.EntityPos;
import myessentials.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Retains the needed information for the action of teleporting a player to a certain position.
 */
public class Teleport extends EntityPos {

    public Teleport(double x, double y, double z, int dim) {
        super(x, y, z, dim);
    }

    /**
     * Executes the teleportation of the given player
     */
    public void execute(EntityPlayerMP player) {
        PlayerUtils.teleport(player, getDim(), getX(), getY(), getZ());
    }
}