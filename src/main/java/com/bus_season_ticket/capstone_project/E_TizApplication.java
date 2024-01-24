package com.bus_season_ticket.capstone_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@SpringBootApplication
@EnableScheduling
public class E_TizApplication {

	public static void main(String[] args) {
		SpringApplication.run(E_TizApplication.class, args);
	}

}
