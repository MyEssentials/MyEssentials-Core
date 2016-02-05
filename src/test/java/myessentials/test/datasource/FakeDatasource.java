package myessentials.test.datasource;

import myessentials.config.api.ConfigTemplate;
import myessentials.datasource.api.DatasourceSQL;
import myessentials.datasource.api.Schema;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FakeDatasource extends DatasourceSQL {

    public FakeDatasource(Logger log, ConfigTemplate config, Schema schema) {
        super(log, config, schema);
    }

    @Override
    public boolean loadAll() {
        //REF: make every load method be able to pass parameters
        return loadBlocks();
    }

    public boolean loadBlocks() {
        try {
            PreparedStatement s = prepare("SELECT * FROM BlockTypes", false);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                //LOG.info(rs.getString("name") + " | " + rs.getString(1));
                DatasourceTest.blockNames.add(rs.getString("name"));
            }
            return true;
        } catch (SQLException ex) {
            //REF: generalize exceptions instead of logging directly to the Logger
            LOG.error("Failed to load BlockTypes");
            LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    public boolean deleteBlocks() {
        try {
            PreparedStatement s = prepare("DELETE FROM BlockTypes", false);
            s.execute();
            return true;
        } catch (SQLException ex) {
            LOG.error("Failed to delete all BlockTypes");
            LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    // REF: ?? Force users to use Objects that package all the data
    public boolean saveBlock(String name, int lightValue, float hardness) {
        try {
            PreparedStatement s = prepare("INSERT INTO BlockTypes VALUES(?, ?, ?)", false);
            s.setString(1, name);
            s.setInt(2, lightValue);
            s.setFloat(3, hardness);
            s.execute();
            return true;
        } catch (SQLException ex) {
            LOG.error("Failed to save BlockType");
            LOG.error(ExceptionUtils.getStackTrace(ex));
            return false;
        }
    }

    @Override
    public boolean checkAll() {
        return false;
    }
}