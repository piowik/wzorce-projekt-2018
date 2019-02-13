package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;

import java.sql.SQLException;

public class MySQLCommit implements Commit {
    private Connection connection;

    MySQLCommit(Connection conn) {
        connection = conn;
    }
    @Override
    public void commit() throws SQLException {
        connection.getConnection().commit();
    }
}
