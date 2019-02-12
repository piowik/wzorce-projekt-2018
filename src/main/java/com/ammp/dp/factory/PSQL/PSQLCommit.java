package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;

import java.sql.SQLException;

public class PSQLCommit implements Commit {
    @Override
    public void createCommit(Connection connection) throws SQLException {
        connection.getConnection().commit();
    }
}
