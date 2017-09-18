package com.nishant.spring.integration.nodsl;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@SpringBootApplication
@IntegrationComponentScan
@EnableIntegration
public class SpringIntegrationClientApplication {

	public static void main(String[] args) throws JsonProcessingException {
		ConfigurableApplicationContext context=SpringApplication.run(SpringIntegrationClientApplication.class, args);
		Gateway channel=context.getBean(Gateway.class);
		Scanner scanner=new Scanner(System.in);
		ObjectMapper obj=new ObjectMapper();
		while(scanner.hasNext()) {
			String line=scanner.nextLine();
			Message<String> message = MessageBuilder.withPayload(line).setHeader(KafkaHeaders.MESSAGE_KEY, "key").setHeader(KafkaHeaders.TOPIC, "nishant").build();
			channel.exchange(obj.writeValueAsString(message));
		}
		scanner.close();

	}
	

	@MessagingGateway(defaultRequestChannel="sendToInboundAdapter.input")
	public interface Gateway{
		public void exchange(String line);
	}

	@Bean
	public IntegrationFlow sendToInboundAdapter() {
		return f -> f.handle(Http.outboundChannelAdapter("http://localhost:8082/sendFromClient"));
	}
}
