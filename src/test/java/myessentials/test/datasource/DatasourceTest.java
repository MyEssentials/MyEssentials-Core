package myessentials.test.datasource;

import org.junit.Assert;
import myessentials.test.MECTest;
import myessentials.test.TestConfig;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
        for (ResourceLocation resource : ForgeRegistries.BLOCKS.getKeys()) {
            datasource.saveBlock(resource.getResourcePath(), 5, 0.0F);
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
//        Assert.assertTrue(datasource.saveBlock(Blocks.DIAMOND_BLOCK.getUnlocalizedName(), Blocks.DIAMOND_BLOCK.getLightValue(), 0.0F));
//        datasource.deleteBlocks();
    }

    @Test
    public void shouldDeleteAllInBlockTypesTable() {
        for (ResourceLocation resource : ForgeRegistries.BLOCKS.getKeys()) {
            datasource.saveBlock(resource.getResourcePath(), 5, 0.0F);
        }
        Assert.assertTrue("Datasource failed to delete all blocks", datasource.deleteBlocks());
    }
}
