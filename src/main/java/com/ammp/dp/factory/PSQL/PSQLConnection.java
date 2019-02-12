package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQLConnection extends Connection {
    private String hostname;
    private String database;
    private String user;
    private String password;
    private java.sql.Connection conn = null;

    public PSQLConnection(String hostname, String database, String user, String password) {
        this.hostname = hostname;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public java.sql.Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conn = DriverManager.getConnection(hostname, user, password);
            } catch (SQLException ex) {
                handleException(ex);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

}
