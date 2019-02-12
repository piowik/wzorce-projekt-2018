package com.ammp.dp.actions;

import com.ammp.dp.factory.*;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PostgreSQLFactory;

import java.sql.ResultSet;
import java.sql.SQLInput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveAccessProtector {

    private Commit commit;
    private Connection connection;
    private Statement statement;

    private SQLFactory sqlFactory;
    private ArrayList<Integer> children;
    private int userID;

    public SaveAccessProtector(String hostname, String user, String password, String dbType) {

        if(dbType.equals(Constants.POSTGRESQL)){
            sqlFactory = new PostgreSQLFactory();
        } else if (dbType.equals(Constants.MYSQL)){
            sqlFactory = new MySQLFactory();
        }

        connection = Objects.requireNonNull(sqlFactory).createConenction(hostname, user, password);
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }


    private void getAllChildren(int id) throws java.sql.SQLException {
        String query = "SELECT * FROM roles WHERE RoleID="+Integer.toString(id);

        // create the java statement
        Statement st = conn.createStatement();

        // execute the query, and get a java resultset
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int ChildID = rs.getInt("ChildID");

        }
    }
        java.sql.Connection conn = connection.getConnection();

}