package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Commit;
import java.sql.SQLException;

public class PSQLCommit implements Commit {
    private com.ammp.dp.factory.Connection connection;

    public PSQLCommit(com.ammp.dp.factory.Connection connection) {
        this.connection = connection;
    }
    @Override
    public void commit() throws SQLException {
        connection.getConnection().commit();
    }
}
