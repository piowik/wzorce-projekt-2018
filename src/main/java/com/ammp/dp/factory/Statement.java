package com.ammp.dp.factory;

import java.sql.SQLException;

public interface Statement {
    public java.sql.Statement getStatement() throws SQLException;
}
