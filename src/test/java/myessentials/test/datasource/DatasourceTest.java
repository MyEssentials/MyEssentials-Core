package myessentials.test.datasource;

import junit.framework.Assert;
import myessentials.test.MECTest;
import myessentials.test.TestConfig;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

//@RunWith(MinecraftRunner.class)
public class DatasourceTest extends MECTest {

    public static List<String> blockNames = new ArrayList<String>();

    private FakeDatasource datasource;

    @Before
    public void shouldInitDatasource() {
        datasource = new FakeDatasource(LOG, TestConfig.instance, new FakeSchema());
        Assert.assertNotNull("Failed to initialise datasource!", datasource);
    }

    @Test
    public void shouldLoadAllInBlockTypesTable() {
        for (String blockName : (Set<String>) Block.blockRegistry.getKeys()) {
            datasource.saveBlock(blockName, 5, 0.0F);
        }

        //REF: ?? public access to loadAll/checkAll methods may be not be needed
        Assert.assertTrue("Datasource failed to load all blocks", datasource.loadBlocks());

        for(String blockName : blockNames) {
            Block block = Block.getBlockFromName(blockName);
            Assert.assertNotNull("Invalid block name got loaded: " + blockName, block);
        }
    }

    @Test
    public void shouldSaveInBlockTypesTable() {
        Assert.assertTrue(datasource.saveBlock(Blocks.diamond_block.getUnlocalizedName(), Blocks.diamond_block.getLightValue(), 0.0F));
        datasource.deleteBlocks();
    }

    @Test
    public void shouldDeleteAllInBlockTypesTable() {
        for (String blockName : (Set<String>) Block.blockRegistry.getKeys()) {
            datasource.saveBlock(blockName, 5, 0.0F);
        }
        Assert.assertTrue("Datasource failed to delete all blocks", datasource.deleteBlocks());
    }
}
