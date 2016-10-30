package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.ChunkPos;
import myessentials.test.MECTest;
import org.junit.Test;

public class ChunkPosTest extends MECTest {

    @Test
    public void chunkPosEquality() {
        ChunkPos cp1 = new ChunkPos(1, 1, 0);
        ChunkPos cp2 = new ChunkPos(1, 1, 0);
        ChunkPos cp3 = new ChunkPos(1, 1, 1);
        ChunkPos cp4 = new ChunkPos(2, 1, 0);

        Assert.assertTrue("Two ChunkPos objects with same coords are not equal", cp1.equals(cp2));
        Assert.assertFalse("Two ChunkPos objects with different dimension id are equal", cp1.equals(cp3));
        Assert.assertFalse("Two ChunkPos objects with different coords are equal", cp1.equals(cp4));
    }
}
