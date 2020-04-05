package com.mohit.coronaVirusdashBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Tell spring to call @sceduled it makes a proxy
public class CoronaVirusDashBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaVirusDashBoardApplication.class, args);
	}

}
