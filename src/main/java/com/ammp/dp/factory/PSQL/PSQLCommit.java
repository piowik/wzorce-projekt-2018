package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;

import java.sql.SQLException;

public class PSQLCommit implements Commit {
    private Connection connection;
    PSQLCommit(Connection conn) {
        connection = conn;
    }
    @Override
    public void commit() throws SQLException {
        connection.getConnection().commit();
    }
}
