package myessentials.datasource.api.bridge;

import com.mysql.jdbc.Driver;
import myessentials.MyEssentialsCore;
import myessentials.config.api.ConfigProperty;
import myessentials.config.api.ConfigTemplate;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.DriverManager;
import java.sql.SQLException;

public class BridgeMySQL extends BridgeSQL {

    public ConfigProperty<String> username = new ConfigProperty<String>(
            "username", "datasource",
            "Username to use when connecting",
            "");

    public ConfigProperty<String> password = new ConfigProperty<String>(
            "password", "datasource",
            "Password to use when connecting",
            "");

    public ConfigProperty<String> host = new ConfigProperty<String>(
            "host", "datasource",
            "Hostname (format: 'host:port') to use when connecting",
            "localhost");

    public ConfigProperty<String> database = new ConfigProperty<String>(
            "database", "datasource",
            "The database name",
            "mytown");

    public BridgeMySQL(ConfigTemplate config) {
        config.addBinding(username);
        config.addBinding(password);
        config.addBinding(host);
        config.addBinding(database, true);

        initProperties();
        initConnection();
    }

    @Override
    protected void initProperties() {
        autoIncrement = "AUTO_INCREMENT";

        properties.put("autoReconnect", "true");
        properties.put("user", username.get());
        properties.put("password", password.get());
        properties.put("relaxAutoCommit", "true");
    }

    @Override
    protected void initConnection() {
        this.dsn = "jdbc:mysql://" + host.get() + "/" + database.get();

        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException ex) {
            MyEssentialsCore.instance.LOG.error("Failed to register driver for MySQL database.", ex);
            MyEssentialsCore.instance.LOG.error(ExceptionUtils.getStackTrace(ex));
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
}
