package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Statement;

import java.sql.SQLException;

public class PSQLStatement implements Statement {

    @Override
    public java.sql.Statement getStatement() throws SQLException {
        return null;
    }
}
