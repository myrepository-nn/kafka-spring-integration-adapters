package com.nishant.spring.integration.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.transformer.MethodInvokingTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ReceiverConfig {
	@Bean
	public IntegrationFlow flow() {
		return IntegrationFlows.from(Http.inboundChannelAdapter("/sendToReceiver")
				.requestMapping(m -> m.methods(HttpMethod.POST))
				.requestPayloadType(String.class)
				)
				.channel(receivedChannel())
				.get();
	}
	@Bean
	public MessageChannel receivedChannel() {
		return new DirectChannel();
	}


	@ServiceActivator(inputChannel = "receivedChannel")
	@Bean
	public MessageHandler service() {
		return new ServiceActivatingHandler(new MethodInvokingTransformer(outputDisplay(), "displayOutput"));
	}
	@Bean
	public OutputDisplay outputDisplay() {
		return new OutputDisplay();
	}

}
