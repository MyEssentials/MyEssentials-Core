package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.BlockPos;
import myessentials.test.MECTest;
import org.junit.Test;

public class BlockPosTest extends MECTest {

    @Test
    public void blockPosEquality() {
        BlockPos bp1 = new BlockPos(1, 1, 1, 0);
        BlockPos bp2 = new BlockPos(1, 1, 1, 0);
        BlockPos bp3 = new BlockPos(1, 1, 1, 1);
        BlockPos bp4 = new BlockPos(1, 2, 1, 0);

        Assert.assertTrue("Two BlockPos objects with same coords are not equal", bp1.equals(bp2));
        Assert.assertFalse("Two BlockPos objects with different dimension id are equal", bp1.equals(bp3));
        Assert.assertFalse("Two BlockPos objects with different coords are equal", bp1.equals(bp4));
    }

}
