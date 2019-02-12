package com.ammp.dp.Statements;

import com.ammp.dp.factory.SQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDBStatement extends DatabaseStatement {
    private SQLFactory factory;
    private java.sql.Statement mysqlStatement;
    public MySQLDBStatement(SQLFactory factory){
        this.factory=factory;
    }
    @Override
    public void prepareStatment() {
        connection = factory.createConenction("","","");
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

    /*@Override
    public ResultSet getResultSet() {
        try {
            return mysqlStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
