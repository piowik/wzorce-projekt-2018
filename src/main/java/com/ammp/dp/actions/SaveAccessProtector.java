package com.ammp.dp.actions;

import com.ammp.dp.Statements.DatabaseStatement;
import com.ammp.dp.Statements.MySQLDBStatement;
import com.ammp.dp.Statements.PSQLDBStatement;
import com.ammp.dp.factory.MySQL.MySQLFactory;
import com.ammp.dp.factory.PSQL.PSQLFactory;

import java.sql.ResultSet;

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
            databaseStatement = new PSQLDBStatement(new PSQLFactory());
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