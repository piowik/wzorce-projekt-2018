package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.SQLFactory;
import com.ammp.dp.factory.Statement;

public class MySQLFactory extends SQLFactory {

    @Override
    public Connection createConenction(String hostname, String database, String user, String password) {
        return new MySQLConnection(hostname, database, user, password);
    }

    @Override
    public Statement createStatement(Connection connection) {
        return new MySQLStatement(connection);
    }

    @Override
    public Commit createCommit(Connection connection) {
        return new MySQLCommit(connection);
    }
}
