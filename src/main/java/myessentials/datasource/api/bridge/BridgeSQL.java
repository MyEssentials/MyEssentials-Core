package myessentials.datasource.api.bridge;

import java.sql.Connection;
import java.util.Properties;

public abstract class BridgeSQL extends Bridge {

    public String prefix = "";
    public String[] userProperties = {};

    public BridgeSQL() {
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
