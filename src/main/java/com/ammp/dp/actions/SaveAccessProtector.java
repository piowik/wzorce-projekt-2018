package com.ammp.dp.actions;

import com.ammp.dp.QueryExtender;
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
    private String userRole;
    private HashMap<String, List<String>> rolesTree;
    private List<String> userAndChildren = new ArrayList<>();
    private String rolesTable = "roles";
    private String roleIdField = "RoleID";
    private String childIdField = "ChildID";
    private String minRoleField = "MinRole";
    private String userRolesTable = "users_roles";
    private String userIdField = "UserID";
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
        String query ="SELECT * FROM " + userRolesTable+" WHERE UserID="+userID;
        databaseStatement.execute(query);
        ResultSet resultSet = databaseStatement.getResultSet();
        this.userID=userID;
        try {
            if(resultSet.next())
                this.userRole=resultSet.getString(roleIdField);
            else
                this.userRole=null;
            }
         catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        rebuildRoles();
    }

    public void rebuildRoles() {
        userAndChildren.clear();
        buildRolesTree();
        userAndChildren.add(userRole);
        fillChildrenByRole(userRole);
    }

    public void configure(String rolesTable, String roleIdField, String childIdField, String minRoleField, String userRolesTable, String userIdField, boolean autoRebuildRoles) {
        this.rolesTable = rolesTable;
        this.roleIdField = roleIdField;
        this.childIdField = childIdField;
        this.minRoleField = minRoleField;
        this.userRolesTable = userRolesTable;
        this.userIdField = userIdField;
        this.autoRebuildRoles = autoRebuildRoles;
    }

    public void buildTableStructure() {
        String createRoles = String.format("CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "  `%s` int(11) NOT NULL,\n" +
                "  `%s` int(11) DEFAULT NULL\n" +
                ")", rolesTable, roleIdField, childIdField);
        System.out.println(createRoles);
        databaseStatement.execute(createRoles);

        String createUserRoles = String.format("CREATE TABLE IF NOT EXISTS `%s` (\n" +
                "  `%s` int(11) NOT NULL,\n" +
                "  `%s` int(11) NOT NULL\n" +
                ")", userRolesTable, userIdField, roleIdField);
        System.out.println(createUserRoles);
        databaseStatement.execute(createUserRoles);

    }

    public void addRolesFields(String[] tableNames, String defaultRole) {
        // if it throws duplicate column then it is user's fault - there is no way to check if column exists AND stay independent to MySQL / postgres version
        for (String tableName : tableNames) {
            String alterQuery = String.format("ALTER TABLE %s ADD %s int(11) %s", tableName, minRoleField, defaultRole);
            databaseStatement.execute(alterQuery);
        }
    }

    private void buildRolesTree() {
        HashMap<String, List<String>> tree = new HashMap<>();
        databaseStatement.execute("SELECT * FROM " + rolesTable);
        ResultSet resultSet = databaseStatement.getResultSet();
        try {
            while (resultSet.next()) {
                String roleID = resultSet.getString(roleIdField);
                String childID = resultSet.getString(childIdField);
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
        if (userRole==null)
            return;
        List<String> children = rolesTree.get(userRole);
        for (String child : children) {
            if (child == null)
                return;
            userAndChildren.add(child);
            fillChildrenByRole(child);
        }
    }

/*    private String prepareRoleConditionWithAnd() {
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
    }*/

    private String prepareCondition() {
        StringBuilder stringBuilder = new StringBuilder(minRoleField + " is null OR " + minRoleField + " in (");
        for (int i = 0; i < userAndChildren.size(); i++) {
            stringBuilder.append("'");
            stringBuilder.append(userAndChildren.get(i));
            stringBuilder.append("'");
            if (i < userAndChildren.size() - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(") ");
        return stringBuilder.toString();
    }

    public ResultSet execute(String query) {
        /*int offset = 2;
        int index = 0;
        String suffixQuery;
        String prefixQuery;
        String finalQuery = "";
        String roleCondition;
        String upperCaseQuery = query.toUpperCase();

        if (upperCaseQuery.contains(Constants.WHERE)) {
            roleCondition = prepareRoleConditionWithAnd();

            index = upperCaseQuery.indexOf(Constants.WHERE);
            int roleConditionIndex = index + Constants.WHERE.length() + offset;

            prefixQuery = query.substring(0, roleConditionIndex - 1);
            suffixQuery = query.substring(roleConditionIndex-1, query.length()-1);
            // TODO: ")" at the end, line below works only when ends with condition
            // eg. select * from example_table where Data is not null or Example_id > 3 order by Example_id;
            //     select * from example_table where (MinRole is null OR MinRole in ('3', '6', '7', '8', '9', '10')) and (Data is not null or Example_id > 3) order by Example_id;
            finalQuery = prefixQuery + " " + roleCondition + " " + suffixQuery + ");";
            System.out.println(finalQuery);
        } else {
            if (upperCaseQuery.contains(Constants.GROUP_BY)) {
                index = upperCaseQuery.indexOf(Constants.GROUP_BY);
            } else if (upperCaseQuery.contains(Constants.ORDER_BY)) {
                index = upperCaseQuery.indexOf(Constants.ORDER_BY);
            }
            roleCondition = prepareRoleConditionWithoutAnd();
            int roleConditionIndex = index - 1;

            prefixQuery = query.substring(0, roleConditionIndex);
            suffixQuery = query.substring(roleConditionIndex);

            finalQuery = prefixQuery + " " + roleCondition + " " + suffixQuery;
        }*/
        if (autoRebuildRoles)
            rebuildRoles();
        query = QueryExtender.extendQuery(query, prepareCondition());
        System.out.println(query);
        databaseStatement.execute(query);
        return databaseStatement.getResultSet();

    }
}