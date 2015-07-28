package myessentials.entities;

/**
 * Helper class for storing position of an entity
 */
public class EntityPos {
    private final int dim;
    private final double x;
    private final double y;
    private final double z;

    public EntityPos(double x, double y, double z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "EntityPos(x: " + x + ", y: " + y + ", z: " + z + " | dim: " + dim + ")";
    }
}
