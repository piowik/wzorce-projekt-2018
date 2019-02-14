package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.SQLFactory;
import com.ammp.dp.factory.Statement;

public class PSQLFactory extends SQLFactory {
    @Override
    public Connection createConnection(String hostname, String database, String user, String password) {
        return new PSQLConnection(hostname, database, user, password);
    }

    @Override
    public Statement createStatement(Connection connection) {
        return new PSQLStatement(connection);
    }

    @Override
    public Commit createCommit(Connection connection) {
        return new PSQLCommit(connection);
    }
}
