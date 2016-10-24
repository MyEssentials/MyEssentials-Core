package myessentials.datasource.api.bridge;

import myessentials.MyEssentialsCore;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeMySQL extends BridgeSQL {

    public String username;
    public String password;
    public String host;
    public String database;

    public BridgeMySQL(CommentedConfigurationNode rootNode) {
        configure(rootNode);

        initProperties();
        initConnection();
    }

    private void configure(CommentedConfigurationNode rootNode) {
        CommentedConfigurationNode usernameNode = rootNode.getNode("datasource", "username");
        CommentedConfigurationNode passwordNode = rootNode.getNode("datasource", "password");
        CommentedConfigurationNode hostNode = rootNode.getNode("datasource", "host");
        CommentedConfigurationNode databaseNode = rootNode.getNode("datasource", "database");

        if (usernameNode.isVirtual()) {
            usernameNode.setValue("")
                        .setComment("Username used for logging into the MySQL database");
        }
        if (passwordNode.isVirtual()) {
            passwordNode.setValue("")
                        .setComment("Password used for logging into the MySQL database");
        }
        if (hostNode.isVirtual()) {
            hostNode.setValue("127.0.0.1")
                    .setComment("Hostname of the MySQL database");
        }
        if (databaseNode.isVirtual()) {
            databaseNode.setValue("myessentials")
                        .setComment("Name of the database used in this mod");
        }
        username = usernameNode.getString();
        password = passwordNode.getString();
        host = hostNode.getString();
        database = databaseNode.getString();
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

        /*try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to register driver for MySQL database.", ex);
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
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
}
