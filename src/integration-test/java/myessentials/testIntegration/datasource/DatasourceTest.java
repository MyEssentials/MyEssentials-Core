package myessentials.testIntegration.datasource;

import myessentials.test.MinecraftRunner;
import net.minecraft.server.MinecraftServer;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(MinecraftRunner.class)
public class DatasourceTest {

    @Test
    public void shouldFail() {
        assert MinecraftServer.getServer() != null;
    }
}
