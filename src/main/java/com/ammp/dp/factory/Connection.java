package com.ammp.dp.factory;

import java.sql.SQLException;

public abstract class Connection {
    public abstract java.sql.Connection getConnection();

    public void handleException(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

}
