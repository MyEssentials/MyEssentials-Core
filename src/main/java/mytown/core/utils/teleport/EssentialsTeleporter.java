package mytown.core.utils.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

/**
 * Created by AfterWind on 4/1/2015.
 * Used for teleporting a player to another dimension
 * Most of it is code from Delpi (from minecraftforge forums). Thank you!
 */
public class EssentialsTeleporter extends Teleporter {

    public EssentialsTeleporter(WorldServer worldserver) {
        super(worldserver);
    }

    // Override Default BS
    @Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        return false;
    }

    @Override
    public void removeStalePortalLocations(long par1)
    {
    }

    @Override
    public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
    }

}