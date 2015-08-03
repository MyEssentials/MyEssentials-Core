package myessentials.test.utils;

import myessentials.entities.ChunkPos;
import myessentials.exception.MyEssentialsTestException;
import myessentials.utils.ClassUtils;
import myessentials.utils.MathUtils;
import myessentials.utils.StringUtils;
import myessentials.utils.WorldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UtilsTest {

    @Test
    public void shouldClassBeLoaded() {
        Assert.assertTrue(ClassUtils.isClassLoaded("myessentials.MyEssentialsCore"));
    }

    @Test
    public void shouldSun() {
        Assert.assertEquals(110, MathUtils.sumFromNtoM(5, 15));
    }

    @Test
    public void shouldParseInt() {
        Assert.assertTrue(StringUtils.tryParseInt("1230045"));
        Assert.assertFalse(StringUtils.tryParseInt("123.0045"));
        Assert.assertFalse(StringUtils.tryParseInt("yyyy712u"));
    }

    @Test
    public void shouldParseFloat() {
        Assert.assertTrue(StringUtils.tryParseFloat("12.55"));
        Assert.assertFalse(StringUtils.tryParseFloat("12, 55"));
        Assert.assertFalse(StringUtils.tryParseFloat("weruu34"));
    }

    @Test
    public void shouldParseBoolean() {
        Assert.assertTrue(StringUtils.tryParseBoolean("false"));
        Assert.assertFalse(StringUtils.tryParseBoolean("ggnore"));
        Assert.assertFalse(StringUtils.tryParseBoolean("00001001011110"));
    }

    @Test
    public void shouldTransformWorldCoordsIntoChunkCoords() throws Exception {
        List<ChunkPos> expectedValues = new ArrayList<ChunkPos>();
        expectedValues.add(new ChunkPos(0, 0, 0));
        expectedValues.add(new ChunkPos(0, 0, 1));
        expectedValues.add(new ChunkPos(0, 0, 2));
        expectedValues.add(new ChunkPos(0, 0, 3));
        expectedValues.add(new ChunkPos(0, 0, 4));

        List<ChunkPos> chunks = WorldUtils.getChunksInBox(0, 10, 10, 10, 64);
        Assert.assertEquals(expectedValues.size(), chunks.size());
        for(int i = 0; i < expectedValues.size(); i++) {
            Assert.assertEquals(expectedValues.get(i).getX(), chunks.get(i).getX());
            Assert.assertEquals(expectedValues.get(i).getZ(), chunks.get(i).getZ());
        }
    }

}
