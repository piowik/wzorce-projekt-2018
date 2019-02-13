package com.ammp.dp;

import com.ammp.dp.actions.Constants;
import com.ammp.dp.actions.SaveAccessProtector;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DpApplication {

    public static void main(String[] args) {
        SaveAccessProtector protecc = SaveAccessProtector.getInstance();
        protecc.connect("harryweb.atthost24.pl", "1404_wzorce", "1404_wzorce", "Wz0rce2018", Constants.MYSQL);
        protecc.setUserID("3");
        protecc.execute("select * from example_table where Data is not null;");
//		SpringApplication.run(DpApplication.class, args);
    }
}
