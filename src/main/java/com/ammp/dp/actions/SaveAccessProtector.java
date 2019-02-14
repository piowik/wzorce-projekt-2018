package com.ammp.dp.actions;

import com.ammp.dp.Statements.DatabaseStatement;
import com.ammp.dp.Statements.MySQLDBStatement;
import com.ammp.dp.Statements.PSQLDBStatement;
import com.ammp.dp.TemplateMethod.QueryDeleteExtender;
import com.ammp.dp.TemplateMethod.QueryExtender;
import com.ammp.dp.TemplateMethod.QuerySelectRegexExtender;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PSQLFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveAccessProtector {
    private DatabaseStatement databaseStatement;
    private String userID;
    private String userRole;
    private HashMap<String, List<String>> rolesTree;
    private List<String> userAndChildren = new ArrayList<>();
    private boolean isAutoCommit = true;
    private boolean autoRebuildRoles = false;


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
    }

    public void setUserID(String userID) {
        this.userID = userID;
        rebuildRoles();
    }

    public void rebuildRoles() {
        getUserRole();
        userAndChildren.clear();
        buildRolesTree();
        userAndChildren.add(userRole);
        fillChildrenByRole(userRole);
    }

    public void setAutoRebuildRoles(boolean value) {
        autoRebuildRoles = value;
    }


    public void buildTableStructure() {
        String createRoles = String.format("CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "  `%s` int(11) NOT NULL,\n" +
                "  `%s` int(11) DEFAULT NULL\n" +
                ")", Constants.ROLES_TABLE, Constants.ROLE_ID_FIELD, Constants.CHILD_ID_FILED);
        System.out.println(createRoles);
        databaseStatement.execute(createRoles);

        String createUserRoles = String.format("CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "  `%s` int(11) NOT NULL,\n" +
                "  `%s` int(11) NOT NULL\n" +
                ")", Constants.USER_ROLES_TABLE, Constants.CHILD_ID_FILED, Constants.ROLE_ID_FIELD);
        System.out.println(createUserRoles);
        databaseStatement.execute(createUserRoles);
        if (!isAutoCommit)
            databaseStatement.commit();
    }

    public void addRolesFields(String[] tableNames, String defaultRole) {
        // if it throws duplicate column then it is user's fault - there is no way to check if column exists AND stay independent to MySQL / postgres version
        for (String tableName : tableNames) {
            String alterQuery = String.format("ALTER TABLE %s ADD %s int(11) %s", tableName, Constants.MIN_ROLE_FIELD, defaultRole);
            databaseStatement.execute(alterQuery);
        }
    }

    private void getUserRole() {
        String query = "SELECT * FROM " + Constants.USER_ROLES_TABLE + " WHERE " + Constants.USER_ID_FIELD + "=" + userID;
        databaseStatement.execute(query);
        ResultSet resultSet = databaseStatement.getResultSet();
        try {
            if (resultSet.next())
                this.userRole = resultSet.getString(Constants.ROLE_ID_FIELD);
            else
                this.userRole = null;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void buildRolesTree() {
        HashMap<String, List<String>> tree = new HashMap<>();
        databaseStatement.execute("SELECT * FROM " + Constants.ROLES_TABLE);
        ResultSet resultSet = databaseStatement.getResultSet();
        try {
            while (resultSet.next()) {
                String roleID = resultSet.getString(Constants.ROLE_ID_FIELD);
                String childID = resultSet.getString(Constants.CHILD_ID_FILED);
                if (tree.containsKey(roleID))
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
        rolesTree = tree;
    }

    private void fillChildrenByRole(String userRole) {
        if (userRole == null)
            return;
        List<String> children = rolesTree.get(userRole);
        for (String child : children) {
            if (child == null)
                return;
            userAndChildren.add(child);
            fillChildrenByRole(child);
        }
    }


    public ResultSet execute(String query, String idName) {
        QueryExtender queryExtender;
        if (query.toUpperCase().contains(Constants.SELECT)) {
            if (autoRebuildRoles) {
                rebuildRoles();
            }
            queryExtender = new QuerySelectRegexExtender(idName);
            query = queryExtender.extendQuery(userAndChildren, userRole, query);
        } else if (query.toUpperCase().contains(Constants.DELETE)) {
            if (autoRebuildRoles) {
                rebuildRoles();
            }
            queryExtender = new QueryDeleteExtender(idName);
            query = queryExtender.extendQuery(userAndChildren, userRole, query);
        }
        System.out.println(query);
        databaseStatement.execute(query);
        return databaseStatement.getResultSet();
    }

    public void setAutoCommit(boolean value) {
        isAutoCommit = value;
        databaseStatement.setAutoCommit(value);
    }

    public void commit() {
        databaseStatement.commit();
    }
}