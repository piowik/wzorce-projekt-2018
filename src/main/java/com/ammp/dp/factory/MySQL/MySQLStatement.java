package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.SQLException;

public class MySQLStatement implements Statement {
    private Connection connection;

    public MySQLStatement(Connection connection) {
        this.connection = connection;
    }

    @Override
    public java.sql.Statement getStatement() throws SQLException {
        return connection.getConnection().createStatement();
    }

}
