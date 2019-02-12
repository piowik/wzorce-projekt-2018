package com.ammp.dp.actions;

import com.ammp.dp.Statements.DatabaseStatement;
import com.ammp.dp.Statements.MySQLDBStatement;
import com.ammp.dp.Statements.PSQLDBStatement;
import com.ammp.dp.factory.Commit;
import com.ammp.dp.factory.Connection;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PostgreSQLFactory;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SaveAccessProtector {
    private DatabaseStatement databaseStatement;
    private int userID;

    private static class Wrapper {
        private static SaveAccessProtector instance = new SaveAccessProtector();
    }

    private SaveAccessProtector() {
    }

    public static SaveAccessProtector getInstance() {
        return Wrapper.instance;
    }

    public void connect(String hostname, String database, String user, String password, String dbType) {
        if (dbType.equals(Constants.POSTGRESQL)) {
            databaseStatement = new PSQLDBStatement(new PostgreSQLFactory());
        } else if (dbType.equals(Constants.MYSQL)) {
            databaseStatement = new MySQLDBStatement(new MySQLFactory());
        }
        databaseStatement.connect(hostname, database, user, password);
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private void buildRolesTree() {
        // TODO: buildRolesTree
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
    }

    public ResultSet execute(String query) {
        //TODO: prepare WHERE statement
        query = query + " WHERE MinRole > 4";
        databaseStatement.execute(query);
        return databaseStatement.getResultSet();
    }
}