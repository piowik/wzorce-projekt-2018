package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Commit;
import java.sql.SQLException;

public class MySQLCommit implements Commit {
    private com.ammp.dp.factory.Connection connection;

    public MySQLCommit(com.ammp.dp.factory.Connection connection) {
        this.connection = connection;
    }
    @Override
    public void commit() throws SQLException {
       connection.getConnection().commit();
    }
}
