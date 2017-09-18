package com.nishant.spring.integration.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegrationManagement;

@SpringBootApplication
@EnableIntegrationManagement
public class SpringIntegrationInboundAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationInboundAdapterApplication.class, args);
	}

}
