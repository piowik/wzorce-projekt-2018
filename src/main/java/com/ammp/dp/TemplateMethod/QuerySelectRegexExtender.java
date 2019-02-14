package com.ammp.dp.TemplateMethod;

import com.ammp.dp.actions.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuerySelectRegexExtender extends QueryExtender {
    private static final String regex = "(select [a-zA-Z*]+(, )*[A-Z,a-z]* from ([A-Za-z_0-9]+))( where ([a-zA-Z]+ (= [a-zA-Z0-9]+|> [a-zA-Z0-9]+|< [a-zA-Z0-9]+|!= [a-zA-Z0-9]+|>= [a-zA-Z0-9]+|<= [a-zA-Z0-9]+|is null|is not null)( and | or )*)+)*(.*)";
    private static final Pattern pattern = Pattern.compile(regex);
    String idname;
    public QuerySelectRegexExtender(String idName) {
        idname = idName;
    }
    String appendConditions(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            StringBuilder outStr = new StringBuilder(matcher.group(1));
            outStr.append(" where (");
            outStr.append(condition);
            outStr.append(matcher.group(3));
            outStr.append("'))");
            if (matcher.group(4) != null) {
                outStr.append(" and (");
                outStr.append(matcher.group(4).replace(" where ", ""));
                outStr.append(")");
            }
            outStr.append(matcher.group(8));
            return outStr.toString();
        }
        return "Error";
    }

    String prepareCondition(List<String> userAndChildren, String userRole) {
        StringBuilder stringBuilder = new StringBuilder(idname);
        stringBuilder.append(" in (SELECT RowId FROM ACL WHERE RoleID in ");
        if (userRole != null) {
            stringBuilder.append("(");
            for (int i = 0; i < userAndChildren.size(); i++) {
                stringBuilder.append("'");
                stringBuilder.append(userAndChildren.get(i));
                stringBuilder.append("'");
                if (i < userAndChildren.size() - 1)
                    stringBuilder.append(", ");
            }
            stringBuilder.append(") and table_name = '");
        }
        return stringBuilder.toString();
    }
}
