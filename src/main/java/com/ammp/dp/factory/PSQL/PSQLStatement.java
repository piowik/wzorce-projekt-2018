package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.SQLException;

public class PSQLStatement implements Statement {
    private Connection connection;
    private java.sql.Statement statement;
    public PSQLStatement(Connection connection){
        this.connection = connection;
    }

    @Override
    public java.sql.Statement getStatement() throws SQLException {
        if(this.statement == null) {
            this.statement = connection.getConnection().createStatement();
        }
        return this.statement;
    }
}
