package com.userservice.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan
public class UserServiceApp {

	public static void main(String[] args) {
		// System.getProperties().put("server.port", 8999);
		System.setProperty("spring.application.name", "User Service");
		System.setProperty("eureka.client.serviceUrl.defaultZone", "http://localhost:8090/eureka");

		ConfigurableApplicationContext context = SpringApplication.run(UserServiceApp.class, args);

	}
}
