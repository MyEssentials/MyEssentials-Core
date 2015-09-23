package myessentials.datasource;

import myessentials.datasource.bridge.BridgeSQL;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DatasourceSQL {

    protected Logger LOG;

    protected String prefix = "";
    protected BridgeSQL bridge;
    protected Schema schema;

    public DatasourceSQL(Logger log, Schema schema) {
        this.LOG = log;
        this.schema = schema;
        loadAll();
        checkAll();
    }

    public abstract boolean loadAll();

    public abstract boolean checkAll();

    protected boolean hasTable(String tableName) {
        try {
            DatabaseMetaData meta = bridge.getConnection().getMetaData();
            ResultSet rs = meta.getTables(null, null, prefix + tableName, null);
            return rs.next();
        } catch (Exception ex) {
            LOG.error("Failed to check for table existence.");
            LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    protected PreparedStatement prepare(String sql, boolean returnGenerationKeys) {
        try {
            return bridge.getConnection().prepareStatement(sql, returnGenerationKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        } catch (SQLException e) {
            LOG.fatal(sql);
            LOG.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    protected void doUpdates() throws SQLException {
        List<String> ids = new ArrayList<String>();
        PreparedStatement statement;
        if(hasTable("Updates")) {
            statement = prepare("SELECT id FROM " + prefix + "Updates", false);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        }

        for (Schema.DBUpdate update : schema.updates) {
            if (ids.contains(update.statement)) {
                continue; // Skip if update is already done
            }

            try {
                LOG.info("Running update {} - {}", update.id, update.desc);
                statement = prepare(update.statement, false);
                statement.execute();

                // Insert the update key so as to not run the update again
                statement = prepare("INSERT INTO " + prefix + "Updates (id,description) VALUES(?,?)", true);
                statement.setString(1, update.id);
                statement.setString(2, update.desc);
                statement.executeUpdate();
            } catch (SQLException e) {
                LOG.error("Update ({} - {}) failed to apply!", update.id, update.desc);
                LOG.error(ExceptionUtils.getStackTrace(e));
                throw e; // Throws back up to force safemode
            }
        }
    }
}
