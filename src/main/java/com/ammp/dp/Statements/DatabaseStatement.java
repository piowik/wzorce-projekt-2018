package com.ammp.dp.Statements;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class DatabaseStatement {
    protected Statement statement;
    protected Commit commit;
    protected Connection connection;

    public abstract void connect(String hostname, String database, String user, String password);

    protected abstract void prepareStatement() throws SQLException;

    public abstract void commit();

    public abstract void setAutoCommit(boolean value);

    public abstract void execute(String query);

    public abstract ResultSet getResultSet();
//    public abstract ResultSet getResultSet();
}
