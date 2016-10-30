package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.Position;
import myessentials.test.MECTest;
import org.junit.Test;

public class PositionTest extends MECTest {

    @Test
    public void blockPosEquality() {
        Position bp1 = new Position(1, 1, 1, 0);
        Position bp2 = new Position(1, 1, 1, 0);
        Position bp3 = new Position(1, 1, 1, 1);
        Position bp4 = new Position(1, 2, 1, 0);

        Assert.assertTrue("Two BlockPos objects with same coords are not equal", bp1.equals(bp2));
        Assert.assertFalse("Two BlockPos objects with different dimension id are equal", bp1.equals(bp3));
        Assert.assertFalse("Two BlockPos objects with different coords are equal", bp1.equals(bp4));
    }

}
