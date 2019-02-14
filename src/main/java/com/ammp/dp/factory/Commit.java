package com.ammp.dp.factory;

import java.sql.SQLException;

public interface Commit {
    void commit() throws SQLException;
}
