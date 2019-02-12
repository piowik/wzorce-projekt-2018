package com.ammp.dp.Statements;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.ResultSet;


public abstract class DatabaseStatement {
    protected Statement statement;
    protected Commit commit;
    protected Connection connection;

    public abstract void prepareStatment(String hostname, String database, String user, String password);
    public abstract void execute(String query );
    public abstract ResultSet getResultSet();
//    public abstract ResultSet getResultSet();
}
