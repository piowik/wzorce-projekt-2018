package com.ammp.dp.TemplateMethod;

import com.ammp.dp.actions.Constants;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuerySelectRegexExtender extends QueryExtender {
    private static final String regex = "(select [a-zA-Z*]+(, )*[A-Z,a-z]* from [A-Za-z_0-9]+)( where ([a-zA-Z]+ (= [a-zA-Z0-9]+|> [a-zA-Z0-9]+|< [a-zA-Z0-9]+|!= [a-zA-Z0-9]+|>= [a-zA-Z0-9]+|<= [a-zA-Z0-9]+|is null|is not null)( and | or )*)+)*(.*)";
    private static final Pattern pattern = Pattern.compile(regex);

    String appendConditions(String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            StringBuilder outStr = new StringBuilder(matcher.group(1));
            outStr.append(" where (");
            outStr.append(condition);
            outStr.append(")");
            if (matcher.group(3) != null) {
                outStr.append(" and (");
                outStr.append(matcher.group(3).replace(" where ", ""));
                outStr.append(")");
            }
            outStr.append(matcher.group(7));
            return outStr.toString();
        }
        return "Error";
    }

    String prepareCondition(List<String> userAndChildren, String userRole) {
        StringBuilder stringBuilder = new StringBuilder(Constants.MIN_ROLE_FIELD);
        stringBuilder.append(" is null");
        if (userRole != null) {
            stringBuilder.append(" OR " + Constants.MIN_ROLE_FIELD + " in (");
            for (int i = 0; i < userAndChildren.size(); i++) {
                stringBuilder.append("'");
                stringBuilder.append(userAndChildren.get(i));
                stringBuilder.append("'");
                if (i < userAndChildren.size() - 1)
                    stringBuilder.append(", ");
            }
            stringBuilder.append(") ");
        }
        return stringBuilder.toString();
    }
}
