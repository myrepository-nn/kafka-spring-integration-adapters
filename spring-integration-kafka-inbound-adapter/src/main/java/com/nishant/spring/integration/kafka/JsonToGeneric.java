package com.nishant.spring.integration.kafka;

import java.io.IOException;

import org.springframework.messaging.Message;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToGeneric {
/*
	public GenericMessage convert(String content) throws JsonParseException, JsonMappingException, IOException {
		GenericMessage gm=new ObjectMapper().readValue(content, GenericMessage.class);
		return gm;
	}
	
*/
	public Message<String> convert(String content) throws JsonParseException, JsonMappingException, IOException {
		Message<String> gm=new ObjectMapper().readValue(content, Message.class);
		return gm;
	}
}