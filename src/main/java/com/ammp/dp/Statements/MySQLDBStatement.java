package com.ammp.dp.Statements;

import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.SQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDBStatement extends DatabaseStatement {
    private SQLFactory factory;
    private java.sql.Statement mysqlStatement;

    public MySQLDBStatement(SQLFactory factory) {
        this.factory = factory;
    }

    @Override
    public void connect(String hostname, String database, String user, String password) {
        connection = factory.createConenction(hostname, database, user, password);
    }

    @Override
    protected void prepareStatment() throws SQLException {
        statement = factory.createStatement(connection);
        mysqlStatement = statement.getStatement();
    }

    @Override
    public void commit() {
        commit = factory.createCommit(connection);
        try {
            commit.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAutoCommit(boolean value) {
        try {
            connection.getConnection().setAutoCommit(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute(String query) {
        try {
            prepareStatment();
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
