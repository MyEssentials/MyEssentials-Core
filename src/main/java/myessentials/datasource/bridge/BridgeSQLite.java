package myessentials.datasource.bridge;

import myessentials.config.ConfigProperty;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sqlite.JDBC;

import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeSQLite extends BridgeSQL {

    @ConfigProperty(category = "datasource.sql", comment = "The database file path. Used by SQLite")
    private String dbPath = "";

    public BridgeSQLite(Configuration config) {
        super(config);
    }

    @Override
    protected void initConnection() {
        this.dsn = "jdbc:sqlite:" + dbPath;

        try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException ex) {
            LOG.error("Failed to register driver for SQLite database.", ex);
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

    @Override
    protected void initProperties() {
        properties.put("foreign_keys", "ON");
    }
}
