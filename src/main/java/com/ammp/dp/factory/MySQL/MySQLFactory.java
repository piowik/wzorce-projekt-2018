package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.*;

public class MySQLFactory extends SQLFactory {

    @Override
    public Connection createConenction(String hostname, String user, String password) {
        return new MySQLConnection();
    }

    @Override
    public Statement createStatement(Connection connection) {
        return new MySQLStatement(connection);
    }

    @Override
    public Commit createCommit() {
        return new MySQLCommit();
    }
}
