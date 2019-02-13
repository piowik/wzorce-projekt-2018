package com.ammp.dp.actions;

import com.ammp.dp.QueryExtender;
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
        StringBuilder stringBuilder = new StringBuilder("MinRole is null OR MinRole in (");
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
       query = QueryExtender.extendQuery(query, prepareCondition());
        System.out.println(query);
        databaseStatement.execute(query);
        return databaseStatement.getResultSet();

    }
}