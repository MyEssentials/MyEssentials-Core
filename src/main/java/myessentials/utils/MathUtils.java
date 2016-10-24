package myessentials.utils;

/**
 * All the utilities related to math equations go here.
 */
public class MathUtils {

    private MathUtils() {
    }

    /**
     * Returns the sum from n to m (Ex: n = 1, m = 3 => 1 + 2 + 3 = 6)
     */
    public static int sumFromNtoM(int n, int m) {
        return Math.abs(m - n + 1) * (m + n) / 2;
    }
}
