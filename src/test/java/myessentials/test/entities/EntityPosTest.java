package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.EntityPos;
import myessentials.test.MECTest;
import org.junit.Test;

public class EntityPosTest extends MECTest {

    @Test
    public void blockPosEquality() {
        EntityPos ep1 = new EntityPos(1.2D, 1.2D, 1.3D, 0);
        EntityPos ep2 = new EntityPos(1.2D, 1.2D, 1.3D, 0);
        EntityPos ep3 = new EntityPos(1.2D, 1.2D, 1.3D, 1);
        EntityPos ep4 = new EntityPos(1.2D, 2.2D, 1.3D, 0);

        Assert.assertTrue("Two BlockPos objects with same coords are not equal", ep1.equals(ep2));
        Assert.assertFalse("Two BlockPos objects with different dimension id are equal", ep1.equals(ep3));
        Assert.assertFalse("Two BlockPos objects with different coords are equal", ep1.equals(ep4));
    }

}
