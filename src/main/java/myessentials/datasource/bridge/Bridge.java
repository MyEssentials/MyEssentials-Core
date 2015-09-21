package myessentials.datasource.bridge;

import myessentials.new_config.ConfigProcessor;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

public abstract class Bridge {

    protected Logger LOG;

    public Bridge(Configuration config) {
        ConfigProcessor.load(getClass(), config);
    }
}
