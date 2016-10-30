package myessentials.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.INpc;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EntityUtils {
    private static ConcurrentMap<Class<?>, String> typeCache = new ConcurrentHashMap<Class<?>, String>();

    public static String getEntityPermission(Entity e) {
        return getEntityType(e).toLowerCase() + "." + getEntityName(e);
    }

    public static String getEntityName(Entity e) {
        String n = e.getCommandSenderEntity().getName();
        if (n.startsWith("entity.") && n.endsWith(".name")) {
            return EntityList.getEntityString(e);
        } else {
            return n;
        }
    }

    public static String getEntityType(Entity e) {
        String type = typeCache.get(e.getClass());
        if (type != null) {
            return type;
        }

        type = getEntityTypeNonCache(e);
        typeCache.put(e.getClass(), type);
        
        return type;
    }
    
    private static String getEntityTypeNonCache(Entity e) {
    	if (e instanceof EntityGolem) {
            return "Golem";
        } else if (e instanceof IAnimals) {
        	return "Animal";
        } else if (e instanceof IMob) {
        	return "Monster";
        } else if (e instanceof IProjectile) {
        	return "Projectile";
        } else if (e instanceof INpc) {
        	return "NPC";
        } else if (e instanceof EntityItem) {
        	return "Item";
        } else if (e instanceof EntityMob) {
        	return "Monster";
        } else if (e instanceof EntityPlayer) {
        	return "Player";
        } else if (e instanceof EntityFireball) {
        	return "Projectile";
        } else if (e instanceof EntityTNTPrimed) {
        	return "TNT";
        } else {
        	return "Unknown"; // e.getClass().getName();
        }
    }
}
