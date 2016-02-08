package myessentials.utils;

/**
 * Useful methods for Chat
 */
public class ChatUtils {

    private ChatUtils() {

    }

    /**
     * Returns true if arg equals on, enable, true, or t. False otherwise.
     */
    // TODO: Overside equal maybe?
    // TODO Change name/change location?
    public static boolean equalsOn(String arg, boolean caseSensitive) {
        if (!caseSensitive)
            arg = arg.toLowerCase();
        return "on".equals(arg) || "enable".equals(arg) || "true".equals(arg) || "t".equals(arg);
    }

    /**
     * Same as {@link ChatUtils#equalsOn(String, boolean)}, but is not case sensitive
     */
    public static boolean equalsOn(String arg) {
        return equalsOn(arg, false);
    }
}
