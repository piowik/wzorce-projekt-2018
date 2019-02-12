package com.ammp.dp.factory;

import java.sql.SQLException;

public interface Commit {
    public void createCommit(Connection connection) throws SQLException;
}
