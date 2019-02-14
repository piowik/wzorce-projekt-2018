package com.ammp.dp.TemplateMethod;

import com.ammp.dp.actions.Constants;

import java.util.List;

public class QueryDeleteExtender extends QueryExtender {
    @Override
    String appendConditions(String input) {
        int offset = 2;
        int index;
        String suffixQuery;
        String prefixQuery;
        String upperCaseQuery = input.toUpperCase();

        if (upperCaseQuery.contains(Constants.WHERE)) {
            index = upperCaseQuery.indexOf(Constants.WHERE);
            int roleConditionIndex = index + Constants.WHERE.length() + offset;
            prefixQuery = input.substring(0, roleConditionIndex - 1);
            suffixQuery = input.substring(roleConditionIndex - 1, input.length() - 1);
            return prefixQuery + " " + condition + suffixQuery + ");";
        } else {
            if(input.contains(";")){
                input = input.replace(";", "");
            }
            return input + condition + ";";
        }
    }


    @Override
    String prepareCondition(List<String> userAndChildren, String userRole) {
        if(input.toUpperCase().contains(Constants.WHERE)){
            StringBuilder stringBuilder = new StringBuilder(" (MinRole is null OR MinRole in (");
            buildConditionFromList(userAndChildren, stringBuilder);
            stringBuilder.append(")) and (");
            return stringBuilder.toString();
        }
        else {
            StringBuilder stringBuilder = new StringBuilder("WHERE (MinRole is null OR MinRole in (");
            buildConditionFromList(userAndChildren, stringBuilder);
            stringBuilder.append(")) ");
            return stringBuilder.toString();
        }

    }

    private void buildConditionFromList(List<String> userAndChildren, StringBuilder stringBuilder) {
        for (int i = 0; i < userAndChildren.size(); i++) {
            stringBuilder.append("'").append(userAndChildren.get(i)).append("'");
            if (i < userAndChildren.size() - 1)
                stringBuilder.append(", ");
        }
    }
}
