package com.ammp.dp;

import com.ammp.dp.actions.Constants;
import com.ammp.dp.actions.SaveAccessProtector;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.ResultSet;

@SpringBootApplication
public class DpApplication {

    public static void main(String[] args) {
        SaveAccessProtector protecc = SaveAccessProtector.getInstance();
        protecc.connect("harryweb.atthost24.pl", "1404_wzorce", "1404_wzorce", "Wz0rce2018", Constants.MYSQL);
        // configure is optional to override default configuration
        protecc.configure("roles_two", "RoleID", "ChildID", "MinRole", "users_roles_two", "UserID", false);
//        protecc.setUserID("3");
//        protecc.buildRoles();
//        ResultSet resultSet = protecc.execute("select * from example_table where Data is not null;");
//        try {
//            while (resultSet.next()) {
//                String data = resultSet.getString("Data");
//                System.out.println(data);
//            }
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }
//		SpringApplication.run(DpApplication.class, args);
    }
}
