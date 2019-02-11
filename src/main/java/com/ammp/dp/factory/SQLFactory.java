package com.ammp.dp.factory;

public abstract class SQLFactory {

    public abstract Connection createConenction(String hostname, String user, String password);
    public abstract Statement createStatement();
    public abstract Commit createCommit();
}
