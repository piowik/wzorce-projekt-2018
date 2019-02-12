package com.ammp.dp.actions;

import com.ammp.dp.Statements.DatabaseStatement;
import com.ammp.dp.Statements.MySQLDBStatement;
import com.ammp.dp.Statements.PSQLDBStatement;
import com.ammp.dp.factory.*;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PostgreSQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SaveAccessProtector {

    private Commit commit;
    private Connection connection;
    private Statement statement;

    private DatabaseStatement databaseStatement;
    private ArrayList<Integer> children;
    private int userID;
    private java.sql.Connection conn;
    private static class Wrapper {
        private static SaveAccessProtector instance = new SaveAccessProtector();
    }

    private SaveAccessProtector() { }

    public static SaveAccessProtector getInstance() {
        return Wrapper.instance;
    }

    public void connect(String hostname, String database, String user, String password, String dbType) {
        if(dbType.equals(Constants.POSTGRESQL)){
            databaseStatement = new PSQLDBStatement(new PostgreSQLFactory());
        } else if (dbType.equals(Constants.MYSQL)){
            databaseStatement = new MySQLDBStatement(new MySQLFactory());
        }
        databaseStatement.connect(hostname, database, user, password);
        databaseStatement.execute("SELECT * FROM roles");
        ResultSet resultSet = databaseStatement.getResultSet();
        try {
            while (resultSet.next()) {
                int childID = resultSet.getInt("ChildID");
                System.out.println("Found " + childID);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        databaseStatement.execute("SELECT * FROM roles");
        ResultSet resultSett = databaseStatement.getResultSet();
        try {
            while (resultSett.next()) {
                int childID = resultSett.getInt("ChildID");
                System.out.println("Found2 " + childID);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
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


}