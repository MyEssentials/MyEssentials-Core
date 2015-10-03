package myessentials.datasource.bridge;

import myessentials.new_config.Config;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sqlite.JDBC;

import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeSQLite extends BridgeSQL {

    public String dbPath = "/media/afterwind/Windows/Users/Sergiu/Documents/GitHub/MyTown2/run/config/MyTown/data.db";

    public BridgeSQLite() {
        initConnection();
        initProperties();
    }

    @Override
    protected void initConnection() {
        this.dsn = "jdbc:sqlite:" + "/media/afterwind/Windows/Users/Sergiu/Documents/GitHub/MyTown2/run/config/MyTown/data.db";

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
