package com.todoservice.greencatsoftware;

import com.todoservice.greencatsoftware.config.OAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(OAuthProperties.class)
public class GreencatsoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreencatsoftwareApplication.class, args);
	}

}
