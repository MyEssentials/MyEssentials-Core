package myessentials.test.datasource;

import myessentials.datasource.api.Schema;
import myessentials.datasource.api.bridge.BridgeSQL;

public class FakeSchema extends Schema {

    @Override
    public void initializeUpdates(BridgeSQL bridge) {
        updates.add(new DBUpdate("07.25.2014.1", "Add Updates Table", "CREATE TABLE IF NOT EXISTS " + bridge.prefix + "Updates (" +
                "id VARCHAR(20) NOT NULL," +
                "description VARCHAR(50) NOT NULL," +
                "PRIMARY KEY(id)" +
                ");"));
        updates.add(new DBUpdate("30.10.2015.1", "Create table BlockTypes", "CREATE TABLE IF NOT EXISTS " + bridge.prefix + "BlockTypes(" +
                "name VARCHAR(50) NOT NULL, " +
                "lightValue INT, " +
                "hardness FLOAT" +
                ")"));
    }
}