package com.ammp.dp.TemplateMethod;

import java.util.List;

public abstract class QueryExtender {
    String condition;
    String input;

    public String extendQuery(List<String> userAndChildren, String userRole, String input){
        this.input = input;
        condition = prepareCondition(userAndChildren, userRole);
        return appendConditions(input);
    }

    abstract String prepareCondition(List<String> userAndChildren, String userRole);
    abstract String appendConditions(String input);

}
