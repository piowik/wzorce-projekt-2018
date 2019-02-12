package com.ammp.dp;

import com.ammp.dp.actions.SaveAccessProtector;

public class test {
    public static void main(String[] args){
        SaveAccessProtector saveAccessProtector = SaveAccessProtector.getInstance();
        String queryTest1 = "Select * from example_table";
        String queryTest2 = "Select * from example_table2";
        String queryTest3 = "Select Data from example_table2 order by Data asc";
        String queryTest4 = "Select Data from example_table2 order by Data desc";


    }
}
