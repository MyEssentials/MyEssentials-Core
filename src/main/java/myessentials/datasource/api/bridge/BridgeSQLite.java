package myessentials.datasource.api.bridge;

import myessentials.Constants;
import myessentials.MyEssentialsCore;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeSQLite extends BridgeSQL {

    public String databasePath;

    public BridgeSQLite(CommentedConfigurationNode rootNode) {
        configure(rootNode);

        initProperties();
        initConnection();
    }

    private void configure(CommentedConfigurationNode rootNode) {
        CommentedConfigurationNode databasePathNode = rootNode.getNode("datasource", "path");
        if (databasePathNode.isVirtual()) {
            databasePathNode.setValue(Constants.DATABASE_FOLDER + "/data.db")
                            .setComment("The path to the database file");
        }
        databasePath = databasePathNode.getString();
    }

    @Override
    protected void initConnection() {
        File file = new File(databasePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        this.dsn = "jdbc:sqlite:" + databasePath;

        /*try {
            DriverManager.registerDriver(new JDBC());
        } catch (SQLException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to register driver for SQLite database.", ex);
        }*/

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
