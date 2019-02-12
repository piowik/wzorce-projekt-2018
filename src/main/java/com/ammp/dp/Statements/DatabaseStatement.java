package com.ammp.dp.Statements;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;


public abstract class DatabaseStatement {
    protected Statement statement;
    protected Commit commit;
    protected Connection connection;

    public abstract void prepareStatment();
    public abstract void execute(String query );
//    public abstract ResultSet getResultSet();
}
