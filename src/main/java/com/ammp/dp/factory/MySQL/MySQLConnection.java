package com.ammp.dp.factory.MySQL;

import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.Statement;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends Connection {
    private static DataSource dataSource;

    @Override
    public java.sql.Connection getConnection() {
        java.sql.Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn =
                    DriverManager.getConnection("jdbc:mysql://localhost/test?" +
                            "user=root&password=password");
        } catch (SQLException ex) {
            handleException(ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

}

