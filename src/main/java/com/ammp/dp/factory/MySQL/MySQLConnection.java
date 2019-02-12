package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends Connection {
    private static final String JDBC_MYSQL = "jdbc:mysql://";

    private String hostname;
    private String user;
    private String password;

    public MySQLConnection(String hostname, String user, String password) {
        this.hostname = hostname;
        this.user = user;
        this.password = password;
    }

    @Override
    public java.sql.Connection getConnection() {
        java.sql.Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(hostname, user, password);
        } catch (SQLException ex) {
            handleException(ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

}

