package com.ammp.dp.factory;

public class MySQLFactory extends SQLFactory {
    @Override
    public Connection createConenction(String hostname, String user, String password) {
        return new MySQLConnection();
    }

    @Override
    public Statement createStatement() {
        return new MySQLStatement();
    }

    @Override
    public Commit createCommit() {
        return new MySQLCommit();
    }
}
