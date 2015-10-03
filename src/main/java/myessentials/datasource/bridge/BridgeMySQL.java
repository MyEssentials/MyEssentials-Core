package myessentials.datasource.bridge;

import com.mysql.jdbc.Driver;
import myessentials.new_config.Config;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeMySQL extends BridgeSQL {

    @Config.Property(name = "username", comment = "Username to use when connecting")
    private String username = "";

    @Config.Property(name = "password", comment = "Password to use when connecting")
    private String password = "";

    @Config.Property(name = "host", comment = "Hostname:Port of the database")
    private String host = "localhost";

    @Config.Property(name = "database", comment = "The database name")
    private String database = "mytown";

    public BridgeMySQL() {
        initConnection();
        initProperties();
    }

    @Override
    protected void initProperties() {
        autoIncrement = "AUTO_INCREMENT";

        properties.put("autoReconnect", "true");
        properties.put("user", username);
        properties.put("password", password);
        properties.put("relaxAutoCommit", "true");
    }

    @Override
    protected void initConnection() {
        this.dsn = "jdbc:mysql://" + host + "/" + database;

        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException ex) {
            LOG.error("Failed to register driver for MySQL database.", ex);
        }

        try {
            if (conn != null && !conn.isClosed()) {
                try {
                    conn.close();
                } catch (SQLException ex) {} // Ignore since we are just closing an old connection
                conn = null;
            }

            conn = DriverManager.getConnection(dsn, properties);
        } catch (SQLException ex) {
            LOG.error("Failed to get SQL connection! {}", dsn);
            LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }
}
