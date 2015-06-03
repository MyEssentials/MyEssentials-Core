package mytown.core.utils;

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
}
