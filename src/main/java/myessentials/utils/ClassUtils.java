package myessentials.utils;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * All utilities exclusively for classes go here
 */
public class ClassUtils {

    private ClassUtils() {
    }

    /**
     * Searches for the class using the path. Example: "net.minecraft.block.Block"
     */
    public static boolean isClassLoaded(String classPath) {
        boolean value;
        try {
            value = Class.forName(classPath) != null;
        } catch (ClassNotFoundException ex) {
            value = false;
        }
        return value;
    }

    public static boolean isBukkitLoaded() {
        return MinecraftServer.getServer().getServerModName().contains("cauldron") || MinecraftServer.getServer().getServerModName().contains("mcpc");
    }

    public static Collection<Class<?>> getAllInterfaces(Class<?> cls) {
    	Set<Class<?>> interfaces = new HashSet<Class<?>>();
    	
    	interfaces.addAll(Arrays.asList(cls.getInterfaces()));

        Class<?> s = cls.getSuperclass();
        if (s != null) {
        	interfaces.addAll(getAllInterfaces(s));
        }

        return interfaces;
    }
}
