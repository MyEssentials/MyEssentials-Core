package myessentials.datasource.api.bridge;

import myessentials.Constants;
import myessentials.MyEssentialsCore;
import myessentials.config.api.ConfigProperty;
import myessentials.config.api.ConfigTemplate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.sqlite.JDBC;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeSQLite extends BridgeSQL {

    public ConfigProperty<String> dbPath = new ConfigProperty<String>(
            "path", "datasource",
            "The path to the database file.",
            "");


    public BridgeSQLite(ConfigTemplate config) {
        dbPath.set(Constants.DATABASE_FOLDER + config.getModID() + "/data.db");
        config.addBinding(dbPath, true);
        initProperties();
        initConnection();
    }

    @Override
    protected void initConnection() {
        File file = new File(dbPath.get());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        this.dsn = "jdbc:sqlite:" + dbPath.get();

        try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to register driver for SQLite database.", ex);
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
            MyEssentialsCore.instance.LOG.error("Failed to get SQL connection! {}", dsn);
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    protected void initProperties() {
        properties.put("foreign_keys", "ON");
    }
}
