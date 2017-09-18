package com.nishant.spring.integration.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegrationManagement;

@SpringBootApplication
@EnableIntegrationManagement
public class SpringIntegrationOutoundAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationOutoundAdapterApplication.class, args);
	}
}
