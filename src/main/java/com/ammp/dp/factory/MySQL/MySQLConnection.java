package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends Connection {
    private static final String JDBC_MYSQL = "jdbc:mysql://";

    private String hostname;
    private String database;
    private String user;
    private String password;

    public MySQLConnection(String hostname, String database, String user, String password) {
        this.hostname = hostname;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    @Override
    public java.sql.Connection getConnection() {
        java.sql.Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = String.format("%s%s/%s?user=%s&password=%s", JDBC_MYSQL, hostname, database, user, password);
            conn = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            handleException(ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

}

