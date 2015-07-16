package myessentials.utils;

/**
 * All utilities related to Strings go here.
 */
public class StringUtils {

    private StringUtils() {
    }

    /**
     * Returns whether or not the String can be parsed as an Integer
     */
    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether or not the String can be parsed as an Float
     */
    public static boolean tryParseFloat(String value) {
        try {
            Float.parseFloat(value);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether or not the String can be parsed as an Boolean
     */
    public static boolean tryParseBoolean(String value) {
        return "true".equals(value) || "false".equals(value);
    }
}
