package com.ammp.dp;

import com.ammp.dp.actions.SaveAccessProtector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DpApplication {

	public static void main(String[] args) {
		SaveAccessProtector gówno = new SaveAccessProtector("harryweb.atthost24.pl","1404_wzorce","Wz0rce2018","mysql");
		SpringApplication.run(DpApplication.class, args);
	}
}
