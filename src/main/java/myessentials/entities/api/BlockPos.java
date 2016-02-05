package myessentials.entities.api;

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

    @Override
    public String toString() {
        return "BlockPos(x: " + x + ", y: " + y + ", z: " + z + " | dim: " + dim + ")";
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof BlockPos) {
            BlockPos otherBP = (BlockPos) other;
            return otherBP.dim == dim && otherBP.x == x && otherBP.y == y && otherBP.z == z;
        }
        return super.equals(other);
    }
}

