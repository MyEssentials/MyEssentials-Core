package myessentials.datasource.bridge;

import myessentials.new_config.Config;
import net.minecraftforge.common.config.Configuration;

import java.sql.Connection;
import java.util.Properties;

public abstract class BridgeSQL extends Bridge {

    @Config.Property(name = "prefix", comment = "The prefix of each of the tables. The format will be: <prefix>[tableName]")
    public String prefix = "";

    @Config.Property(name = "prefix", comment = "User defined properties to be passed to the connection.\nFormat: key=value;key=value...")
    public String[] userProperties = {};

    public BridgeSQL(Configuration config) {
        super(config);
        initProperties();
        initConnection();
    }

    protected Properties properties = new Properties();
    protected String dsn = "";
    protected Connection conn = null;
    protected String autoIncrement;

    protected abstract void initConnection();
    protected abstract void initProperties();

    public Connection getConnection() {
        return this.conn;
    }

    public String getAutoIncrement() {
        return this.autoIncrement;
    }
}
