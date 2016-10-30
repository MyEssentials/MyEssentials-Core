package myessentials.test.entities;

import junit.framework.Assert;
import myessentials.entities.api.Volume;
import myessentials.test.MECTest;
import org.junit.Test;

public class VolumeTest extends MECTest {

    @Test
    public void volumeEquality() {

        Volume v1 = new Volume(0, 0, 0, 2, 2, 2);
        Volume v2 = new Volume(0, 0, 0, 2, 2, 2);
        Volume v3 = new Volume(0, 0, 0, 2, 4, 4);

        Assert.assertTrue("Two volume object with same coords are not equal", v1.equals(v2));
        Assert.assertFalse("Two volume object with different coords are equal", v1.equals(v3));
    }

    @Test
    public void shouldIntersect() {

        Volume v1 = new Volume(0, 0, 0, 5, 5, 5);
        Volume v2 = new Volume(2, 2, 2, 6, 6, 6);

        Volume intersection = v1.intersect(v2);

        Assert.assertTrue("Intersection of two volumes was wrong", intersection.getMaxX() == 5 && intersection.getMaxY() == 5 && intersection.getMaxZ() == 5 && intersection.getMinX() == 2 && intersection.getMinY() == 2 && intersection.getMinZ() == 2);
    }

    @Test
    public void shouldHaveProperVolumeAmount() {

        Volume v1 = new Volume(4, 4, 4, 9, 9, 9);

        Assert.assertEquals("Volume amount was wrong", 216, v1.getVolumeAmount());
    }
}
