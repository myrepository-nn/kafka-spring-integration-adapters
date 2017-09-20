package com.nishant.spring.integration.kafka;

import java.io.IOException;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JsonToGeneric {

	public Message<String> convert(String content) throws JsonParseException, JsonMappingException, IOException {

		Message<String> gm=new GenericMessage<>(content);
		MessageHeaderAccessor mha=new MessageHeaderAccessor(gm);

		return MessageBuilder.withPayload(gm.getPayload()).setHeaders(mha).setHeader(KafkaHeaders.MESSAGE_KEY, "key").setHeader(KafkaHeaders.TOPIC, "nishant").build();
	}
}