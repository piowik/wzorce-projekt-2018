package com.ammp.dp.actions;

import com.ammp.dp.actions.Constants;
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

    private String prepareRoleConditionWithAnd() {
        StringBuilder stringBuilder = new StringBuilder(" (MinRole is null OR MinRole in (");
        for (int i = 0; i < userAndChildren.size(); i++) {
            stringBuilder.append("'" + userAndChildren.get(i) + "'");
            if (i < userAndChildren.size() - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(")) and (");
        return stringBuilder.toString();
    }

    private String prepareRoleConditionWithoutAnd() {
        StringBuilder stringBuilder = new StringBuilder("WHERE MinRole is null OR MinRole in (");
        for (int i = 0; i < userAndChildren.size(); i++) {
            stringBuilder.append("'" + userAndChildren.get(i) + "'");
            if (i < userAndChildren.size() - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(") ");
        return stringBuilder.toString();
    }

    public ResultSet execute(String query) {
        int offset = 2;
        int index = 0;
        String suffixQuery;
        String prefixQuery;
        String finalQuery = "";
        String roleCondition;
        query = query.toUpperCase();

        if (query.contains(Constants.WHERE)) {
            roleCondition = prepareRoleConditionWithAnd();

            index = query.indexOf(Constants.WHERE);
            int roleConditionIndex = index + Constants.WHERE.length() + offset;

            prefixQuery = query.substring(0, roleConditionIndex - 1);
            suffixQuery = query.substring(roleConditionIndex);
            // TODO: ")" at the end
            finalQuery = prefixQuery + " " + roleCondition + " " + suffixQuery;
        } else {
            if (query.contains(Constants.GROUP_BY)) {
                index = query.indexOf(Constants.GROUP_BY);
            } else if (query.contains(Constants.ORDER_BY)) {
                index = query.indexOf(Constants.ORDER_BY);
            }
            roleCondition = prepareRoleConditionWithoutAnd();
            int roleConditionIndex = index - 1;

            prefixQuery = query.substring(0, roleConditionIndex);
            suffixQuery = query.substring(roleConditionIndex);

            finalQuery = prefixQuery + " " + roleCondition + " " + suffixQuery;
        }

        databaseStatement.execute(finalQuery);
        return databaseStatement.getResultSet();

    }
}