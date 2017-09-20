package com.nishant.spring.integration.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter;
import org.springframework.integration.kafka.inbound.KafkaMessageDrivenChannelAdapter.ListenerMode;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.messaging.MessageChannel;
@Configuration
@EnableKafka
public class OutboundAdapterConfig {
	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String,Object> conf=new HashMap<>();
		conf.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		conf.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		conf.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		conf.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		conf.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
		return new DefaultKafkaConsumerFactory<>(conf);
	}
	@Bean
	public KafkaMessageListenerContainer<String, String> container() throws Exception {
		ContainerProperties properties = new ContainerProperties("nishant");
		return new KafkaMessageListenerContainer<>(consumerFactory(), properties);
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, String> ckl=new ConcurrentKafkaListenerContainerFactory<>();
		ckl.setConcurrency(3);
		ckl.getContainerProperties().setPollTimeout(3000);
		ckl.setConsumerFactory(consumerFactory());
		return ckl;
	}

	@Bean
	public MessageChannel messgeChannel() {
		return new DirectChannel();
	}

	@Bean
	public KafkaMessageDrivenChannelAdapter<String, String>
	adapter(KafkaMessageListenerContainer<String, String> container) throws Exception {
		KafkaMessageDrivenChannelAdapter<String, String> kafkaMessageDrivenChannelAdapter =
				new KafkaMessageDrivenChannelAdapter<>(container(), ListenerMode.record);
		kafkaMessageDrivenChannelAdapter.setOutputChannel(messgeChannel());
		return kafkaMessageDrivenChannelAdapter;
	}

	@Bean
	public IntegrationFlow flow() {
		return f -> f.channel(messgeChannel()).handle(Http.outboundChannelAdapter("http://localhost:8084/sendToReceiver"));
	}
}
