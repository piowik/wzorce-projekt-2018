package com.ammp.dp.factory;

public class PostgreSQLFactory extends SQLFactory {
    @Override
    public Connection createConenction(String hostname, String user, String password) {
        return new PSQLConnection(hostname, user, password);
    }

    @Override
    public Statement createStatement() {
        return new PSQLStatement();
    }

    @Override
    public Commit createCommit() {
        return new PSQLCommit();
    }
}
