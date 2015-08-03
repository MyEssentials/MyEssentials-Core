package myessentials.entities;

/**
 * Helper class for storing position of a chunk
 */
public class ChunkPos {
    private final int dim;
    private final int x;
    private final int z;

    public ChunkPos(int dim, int x, int z) {
        this.dim = dim;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getDim() {
        return dim;
    }

    @Override
    public String toString() {
        return "ChunkPos(x: " + x + ", z: " + z + " | Dim: " + dim + ")";
    }
}
