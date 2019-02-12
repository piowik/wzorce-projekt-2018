package com.ammp.dp.Statements;

import com.ammp.dp.factory.PSQL.PostgreSQLFactory;
import com.ammp.dp.factory.SQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PSQLDBStatement extends DatabaseStatement {
    private SQLFactory factory;
    private java.sql.Statement mysqlStatement;
    public PSQLDBStatement(PostgreSQLFactory factory){
        this.factory=factory;
    }
    @Override
    public void prepareStatment(String hostname, String database, String user, String password) {
        connection = factory.createConenction(hostname, database, user, password);
        statement = factory.createStatement(connection);
        try {
            mysqlStatement = statement.getStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //        commit = factory.createCommit();

    }

    @Override
    public void execute(String query) {
        try {
            mysqlStatement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet getResultSet() {
        try {
            return mysqlStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
