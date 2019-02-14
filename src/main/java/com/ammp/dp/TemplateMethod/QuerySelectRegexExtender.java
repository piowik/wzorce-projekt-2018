package com.ammp.dp.TemplateMethod;

import com.ammp.dp.actions.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuerySelectRegexExtender extends QueryExtender{
    private static final String regex = "(select [a-zA-Z\\*]+(, )*[A-Z,a-z]* from [A-Za-z_0-9]+)( where ([a-zA-Z]+ (= [a-zA-Z0-9]+|> [a-zA-Z0-9]+|< [a-zA-Z0-9]+|!= [a-zA-Z0-9]+|>= [a-zA-Z0-9]+|<= [a-zA-Z0-9]+|is null|is not null)( and | or )*)+)*(.*)";
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



    private static ArrayList<String> getTestStrings() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("select * from users;");
        strings.add("select * from users order BY id;");
        strings.add("select id from users;");
        strings.add("select id from users order BY id;");
        strings.add("select id, name from users;");
        strings.add("select id, name from users order BY id;");
        strings.add("select * from users where id = 15;");
        strings.add("select * from users where id = 12 ORDER BY id;");
        strings.add("select id from users where id = 1;");
        strings.add("select id from users where id = 1 ORDER BY id;");
        strings.add("select id, name from users where id = 10;");
        strings.add("select id, name from users where id = 12 ORDER BY id;");
        strings.add("select id, name from users where id = 14 or id = 2;");
        strings.add("select id, name from users where id = 14 and id >= 2;");
        strings.add("select id, name from users where id >= 14 or id = 2;");
        strings.add("select id, name from users where id = 16 or id = 2 ORDER BY id;");
        strings.add("select id, name from users where id = 16 or id is null ORDER BY id;");
        strings.add("select id, name from users where id = 16 or id is not null ORDER BY id;");
        strings.add("select id, name from users where id is null and id is not null ORDER BY id;");
        return strings;
    }


//    public void main(String[] args) {
//        ArrayList<String> strings = getTestStrings();
//        ArrayList<String> output = new ArrayList<>();
//        String conditionString = "id < 0 and id in (0,1,2)";
//        for (String s : strings) {
//            if (s.toLowerCase().contains("where"))
//                output.add(extendQuery(s, conditionString));
//            else
//                output.add(extendQuery(s, conditionString));
//        }
//        for (int i = 0; i < strings.size(); i++) {
//            System.out.println("In: " + strings.get(i));
//            System.out.println("Out: " + output.get(i));
//        }
//    }


}
