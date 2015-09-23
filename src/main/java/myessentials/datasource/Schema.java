package myessentials.datasource;

import myessentials.datasource.bridge.BridgeSQL;

import java.util.ArrayList;
import java.util.List;

public abstract class Schema {

    protected List<DBUpdate> updates = new ArrayList<DBUpdate>();

    public abstract void initializeUpdates(BridgeSQL bridge);

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
