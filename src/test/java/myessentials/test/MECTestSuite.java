package myessentials.test;

import metest.BaseSuite;
import myessentials.test.chat.JsonMessageBuilderTest;
import myessentials.test.config.ConfigTest;
import myessentials.test.datasource.DatasourceTest;
import myessentials.test.economy.EconomyForgeEssentialsTest;
import myessentials.test.economy.EconomyItemTest;
import myessentials.test.entities.BlockPosTest;
import myessentials.test.entities.EntityPosTest;
import myessentials.test.entities.TreeTest;
import myessentials.test.entities.VolumeTest;
import myessentials.test.entities.sign.SignTest;
import myessentials.test.entities.tool.ToolTest;
import myessentials.test.json.JsonConfigTest;
import myessentials.test.json.SerializerTest;
import myessentials.test.utils.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({

        EconomyItemTest.class,
        EconomyForgeEssentialsTest.class,
        DatasourceTest.class,
        ConfigTest.class,
        SignTest.class,
        ToolTest.class,
        BlockPosTest.class,
        ChunkPosTest.class,
        EntityPosTest.class,
        TreeTest.class,
        VolumeTest.class,
        JsonMessageBuilderTest.class,
        JsonConfigTest.class,
        SerializerTest.class,
        ClassUtilsTest.class,
        ItemUtilsTest.class,
        MathUtilsTest.class,
        PlayerUtilsTest.class,
        StringUtilsTest.class,
        WorldUtilsTest.class

})

public class MECTestSuite extends BaseSuite {
}
