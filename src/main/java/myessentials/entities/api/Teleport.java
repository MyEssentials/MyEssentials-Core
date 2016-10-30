package myessentials.entities.api;

import myessentials.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Retains the needed information for the action of teleporting a player to a certain position.
 */
public class Teleport extends Position {
    public Teleport(double x, double y, double z, int dim) {
        super(x, y, z, dim);
    }

    public Teleport(Entity entity) {
        super(entity);
    }

    /**
     * Executes the teleportation of the given player
     */
    public void execute(EntityPlayerMP player) {
        PlayerUtils.teleportEntity(player, this);
    }
}
