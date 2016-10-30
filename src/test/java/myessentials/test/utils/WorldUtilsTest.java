package myessentials.test.utils;

import myessentials.entities.api.ChunkPos;
import myessentials.test.MECTest;
import myessentials.utils.WorldUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WorldUtilsTest extends MECTest {

    @Test
    public void shouldTransformWorldCoordsIntoChunkCoords() {
        List<ChunkPos> expectedValues = new ArrayList<ChunkPos>();
        expectedValues.add(new ChunkPos(0, 0, 0));
        expectedValues.add(new ChunkPos(0, 0, 1));
        expectedValues.add(new ChunkPos(0, 0, 2));
        expectedValues.add(new ChunkPos(0, 0, 3));
        expectedValues.add(new ChunkPos(0, 0, 4));

        List<ChunkPos> chunks = WorldUtils.getChunksInBox(0, 10, 10, 10, 64);
        Assert.assertEquals(expectedValues.size(), chunks.size());
        for(int i = 0; i < expectedValues.size(); i++) {
            Assert.assertEquals(expectedValues.get(i).x, chunks.get(i).x);
            Assert.assertEquals(expectedValues.get(i).z, chunks.get(i).z);
        }
    }

    @Test
    public void shouldGetMaxHeightWithSolid() {

        World world = server.worldServerForDimension(0);
        world.setBlockState(new BlockPos(10, 240, 10), Blocks.STONE.getDefaultState());
        for (int i = 241; i < 256; i++) {
            world.setBlockState(new BlockPos(10, i, 10), Blocks.AIR.getDefaultState());
        }

        int maxY = WorldUtils.getMaxHeightWithSolid(0, 10, 10);
        Assert.assertEquals("Max height that is solid is not the right one", 240, maxY);

    }

}
