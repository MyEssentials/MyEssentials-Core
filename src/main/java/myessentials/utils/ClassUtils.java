package myessentials.utils;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<Class<?>> getAllInterfaces(Class<?> cls) {
        List<Class<?>> lst = new ArrayList<Class<?>>();

        lst.addAll(Arrays.asList(cls.getInterfaces()));

        Class<?> s = cls.getSuperclass();
        if (s != null) {
            lst.addAll(getAllInterfaces(s));
        }

        return lst;
    }
}
