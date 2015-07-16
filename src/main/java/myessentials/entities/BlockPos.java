package myessentials.entities;

/**
 * Helper class for storing position of a block
 */
public class BlockPos {
    private final int dim;
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
    }

    public int getDim() {
        return dim;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}

