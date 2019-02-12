package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.PSQL.PSQLCommit;
import com.ammp.dp.factory.PSQL.PSQLConnection;
import com.ammp.dp.factory.PSQL.PSQLStatement;
import com.ammp.dp.factory.SQLFactory;
import com.ammp.dp.factory.Statement;

public class PostgreSQLFactory extends SQLFactory {
    @Override
    public Connection createConenction(String hostname, String database, String user, String password) {
        return new PSQLConnection(hostname, database, user, password);
    }

    @Override
    public Statement createStatement(Connection connection) {
        return null;
    }

    @Override
    public Commit createCommit() {
        return new PSQLCommit();
    }
}
