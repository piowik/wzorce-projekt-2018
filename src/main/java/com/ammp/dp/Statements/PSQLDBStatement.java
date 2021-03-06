package com.ammp.dp.Statements;

import com.ammp.dp.factory.PSQL.PSQLFactory;
import com.ammp.dp.factory.SQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PSQLDBStatement extends DatabaseStatement {
    private SQLFactory factory;
    private java.sql.Statement mysqlStatement;

    public PSQLDBStatement(PSQLFactory factory) {
        this.factory = factory;
    }

    @Override
    public void connect(String hostname, String database, String user, String password) {
        connection = factory.createConnection(hostname, database, user, password);
    }

    @Override
    protected void prepareStatement() throws SQLException {
        statement = factory.createStatement(connection);
        mysqlStatement = statement.getStatement();
    }

    @Override
    public void execute(String query) {
        try {
            prepareStatement();
            mysqlStatement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public ResultSet getResultSet() {
        try {
            return mysqlStatement.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
