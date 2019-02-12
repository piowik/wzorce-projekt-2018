package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLStatement implements Statement {
    private Connection connection;
    private java.sql.Statement statement;
    public MySQLStatement(Connection connection){
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
