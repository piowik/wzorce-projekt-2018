package com.ammp.dp.factory;

import java.sql.SQLException;

public interface Statement {
    java.sql.Statement getStatement() throws SQLException;
}
