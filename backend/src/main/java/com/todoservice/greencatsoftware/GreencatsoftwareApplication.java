package com.todoservice.greencatsoftware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GreencatsoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreencatsoftwareApplication.class, args);
	}

}
