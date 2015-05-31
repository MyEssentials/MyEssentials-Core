package mytown.core.utils;

import mytown.core.utils.teleport.EssentialsTeleporter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by joe on 4/5/15.
 * All utilities that are exclusively for Entity and not for EntityPlayer go here.
 */
public class EntityUtils {
    /**
     * Drops the specified itemstack in the worls as a EntityItem
     */
    public static void dropAsEntity(World world, int x, int y, int z, ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }
        double f = 0.7D;
        double dx = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
        double dy = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;
        double dz = world.rand.nextFloat() * f + (1.0D - f) * 0.5D;

        EntityItem entityItem = new EntityItem(world, x + dx, y + dy, z + dz, itemStack);
        //entityItem.field_145804_b = 10;
        world.spawnEntityInWorld(entityItem);
    }
}
