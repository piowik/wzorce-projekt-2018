package com.ammp.dp.factory.PSQL;

import com.ammp.dp.factory.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQLConnection extends Connection {
    private String hostname;
    private String user;
    private String password;

    public PSQLConnection(String hostname, String user, String password) {
        this.hostname = hostname;
        this.user = user;
        this.password = password;
    }

    @Override
        public java.sql.Connection getConnection(){
            java.sql.Connection conn = null;
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
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
