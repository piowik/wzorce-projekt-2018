package com.ammp.dp;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryExtender {
    /*private static final String regexNoWhere = "(select [a-zA-Z\\*]+(, )*[A-Z,a-z]* from [A-Za-z_]+[ ]*)(.*)";
    private static final Pattern patternNoWhere = Pattern.compile(regexNoWhere);
    private static final String regexWhere = "(select [a-zA-Z\\*]+(, )*[A-Z,a-z]* from [A-Za-z_]+) (where ([a-zA-Z]+ (= [a-zA-Z0-9]+|> [a-zA-Z0-9]+|< [a-zA-Z0-9]+|>= [a-zA-Z0-9]+|<= [a-zA-Z0-9]+|is null|is not null)( and | or )*)+)(.*)";
    private static final Pattern patternWhere = Pattern.compile(regexWhere);*/
    private static final String regex = "(select [a-zA-Z\\*]+(, )*[A-Z,a-z]* from [A-Za-z_0-9]+)( where ([a-zA-Z]+ (= [a-zA-Z0-9]+|> [a-zA-Z0-9]+|< [a-zA-Z0-9]+|>= [a-zA-Z0-9]+|<= [a-zA-Z0-9]+|is null|is not null)( and | or )*)+)*(.*)";
    private static final Pattern pattern = Pattern.compile(regex);

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

    public static String extendQuery(String input, String condition) {
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

    /*public static String addWhere(String input, String condition) {
        Matcher matcher = patternNoWhere.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1) + " where (" + condition + ") " + matcher.group(3);
        }
        return "Error";
    }

    public static String extendWhere(String input, String condition) {
        Matcher matcher = patternWhere.matcher(input);
        if (matcher.matches()) {
            return matcher.group(1) + " where (" + condition + ") and (" + matcher.group(3).replace("where ", "") + ")" + matcher.group(7);

        }
        return "Error";
    }*/

    public static void main(String[] args) {
        ArrayList<String> strings = getTestStrings();
        ArrayList<String> output = new ArrayList<>();
        String conditionString = "id < 0 and id in (0,1,2)";
        for (String s : strings) {
            if (s.toLowerCase().contains("where"))
                output.add(extendQuery(s, conditionString));
            else
                output.add(extendQuery(s, conditionString));
        }
        for (int i = 0; i < strings.size(); i++) {
            System.out.println("In: " + strings.get(i));
            System.out.println("Out: " + output.get(i));
        }
    }


}
