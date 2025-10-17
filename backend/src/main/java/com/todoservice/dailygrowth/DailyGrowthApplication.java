package com.todoservice.dailygrowth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DailyGrowthApplication {
	public static void main(String[] args) {
		SpringApplication.run(DailyGrowthApplication.class, args);
	}
}