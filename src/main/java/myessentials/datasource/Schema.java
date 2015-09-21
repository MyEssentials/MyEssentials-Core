package myessentials.datasource;

import myessentials.datasource.bridge.BridgeSQL;

import java.util.ArrayList;
import java.util.List;

public abstract class Schema {

    protected BridgeSQL bridge;
    protected List<DBUpdate> updates = new ArrayList<DBUpdate>();

    public Schema(BridgeSQL bridge) {
        this.bridge = bridge;
        initializeUpdates();
    }

    public abstract void initializeUpdates();

    public class DBUpdate {
        /**
         * Formatted mm.dd.yyyy.e where e increments by 1 for every update released on the same date
         */
        public final String id;
        public final String desc;
        public final String statement;

        public DBUpdate(String id, String desc, String statement) {
            this.id = id;
            this.desc = desc;
            this.statement = statement;
        }
    }
}
