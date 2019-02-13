package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.SQLException;

public class PSQLStatement implements Statement {
    private Connection connection;

    public PSQLStatement(Connection connection) {
        this.connection = connection;
    }

    @Override
    public java.sql.Statement getStatement() throws SQLException {
        return connection.getConnection().createStatement();
    }
}
