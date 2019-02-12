package com.ammp.dp;

import com.ammp.dp.actions.Constants;
import com.ammp.dp.actions.SaveAccessProtector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DpApplication {

	public static void main(String[] args) {
        SaveAccessProtector protecc = SaveAccessProtector.getInstance();
        protecc.connect("harryweb.atthost24.pl", "1404_wzorce","1404_wzorce","Wz0rce2018", Constants.MYSQL);
//		SpringApplication.run(DpApplication.class, args);
	}
}
