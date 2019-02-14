package com.ammp.dp;

import com.ammp.dp.actions.Constants;
import com.ammp.dp.actions.SaveAccessProtector;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.ResultSet;

@SpringBootApplication
public class DpApplication {
    private static final SaveAccessProtector protecc = SaveAccessProtector.getInstance();

    private static void exampleFirstTimeUse() {
        // example initialization for first time use (no tables, no columns)
        protecc.buildTableStructure();
        String[] tableNames = {"example_table3"};
        protecc.addRolesFields(tableNames, "null");
    }


    public static void main(String[] args) {
        protecc.connect("harryweb.atthost24.pl", "1404_wzorce", "1404_wzorce", "Wz0rce2018", Constants.MYSQL);

        protecc.setUserID("3");

        // OPTIONAL EXAMPLES START
        // rebuilding manually user roles
//        protecc.rebuildRoles();

        // disabling auto commit and committing
//        protecc.setAutoCommit(false);
//        protecc.commit();
        // OPTIONAL EXAMPLES END

        ResultSet resultSet = protecc.execute("select * from example_table");
//        ResultSet resultSet = protecc.execute("delete from example_table where Data= \'dane1_1\' ");
        if(resultSet != null) {
            try {
                while (resultSet.next()) {
                    String data = resultSet.getString("Data");
                    System.out.println(data);
                }
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
//		SpringApplication.run(DpApplication.class, args);
    }
}
