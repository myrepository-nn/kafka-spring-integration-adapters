package com.nishant.spring.integration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.integration.transformer.MethodInvokingTransformer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
public class InboundAdapterConfig {
	@Bean
	public IntegrationFlow flow() {
		return IntegrationFlows.from(Http.inboundChannelAdapter("/sendFromClient")
				.requestMapping(m -> m.methods(HttpMethod.POST))
				.requestPayloadType(String.class)
				)
				.channel(receivedInAdapter())
				.get();
	}

	@Bean
	public MessageChannel receivedInAdapter() {
		return new DirectChannel();
	}

	@Transformer(inputChannel="receivedInAdapter")
	@Bean
	public MessageHandler transform() {
		MessageTransformingHandler mth=new MessageTransformingHandler(new MethodInvokingTransformer(jsonToGeneric(), "convert"));
		mth.setOutputChannel(inputToKafka());
		return mth;
	}


	@Bean
	public MessageChannel inputToKafka() {
		return new DirectChannel();
	}

	@ServiceActivator(inputChannel = "inputToKafka")
	@Bean
	public MessageHandler handler() throws Exception {
		return new KafkaProducerMessageHandler<String, String>(kafkaTemplate());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> conf = new HashMap<>();
		conf.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return new DefaultKafkaProducerFactory<>(conf);
	}
	@Bean
	public JsonToGeneric jsonToGeneric() {
		return new JsonToGeneric();
	}

}
