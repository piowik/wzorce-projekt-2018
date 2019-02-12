package com.ammp.dp.actions;

import com.ammp.dp.Statements.DatabaseStatement;
import com.ammp.dp.Statements.MySQLDBStatement;
import com.ammp.dp.Statements.PSQLDBStatement;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PSQLFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveAccessProtector {
    private DatabaseStatement databaseStatement;
    private String userID;
    private HashMap<String,List<String>> rolesTree;
    private List<String> userAndChildren;

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
            databaseStatement = new PSQLDBStatement(new PSQLFactory());
        } else if (dbType.equals(Constants.MYSQL)) {
            databaseStatement = new MySQLDBStatement(new MySQLFactory());
        }
        databaseStatement.connect(hostname, database, user, password);
        buildRolesTree();
    }

    public void setUserID(String userID) {
        this.userID = userID;
        userAndChildren=new ArrayList<>();
        userAndChildren.add(userID);
        fillChildrenByID(userID);
    }

    private void buildRolesTree() {
        HashMap<String,List<String>> tree=new HashMap<>();
        databaseStatement.execute("SELECT * FROM roles");
        ResultSet resultSet = databaseStatement.getResultSet();
        try {
            while (resultSet.next()) {
                String roleID = resultSet.getString("RoleID");
                String childID = resultSet.getString("ChildID");
                if(tree.containsKey(roleID))
                    tree.get(roleID).add(childID);
                else {
                    List<String> newChildren = new ArrayList<>();
                    newChildren.add(childID);
                    tree.put(roleID, newChildren);
                }
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        rolesTree=tree;
    }
    
    private void fillChildrenByID(String userID) {
        List<String> children = rolesTree.get(userID);
        for (String child: children ) {
            if(child==null)
                return;
            userAndChildren.add(child);
            fillChildrenByID(child);
        }
    }

    public ResultSet execute(String query) {
        //TODO: prepare WHERE statement (check if where already exists, group by, limit, order etc)
        query = query + " WHERE MinRole > 4";
        databaseStatement.execute(query);
        return databaseStatement.getResultSet();
    }
}