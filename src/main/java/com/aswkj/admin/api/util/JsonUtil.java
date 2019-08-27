package com.aswkj.admin.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class JsonUtil {
	
	private static ObjectMapper mapper = new ObjectMapper();

	static {

		//时间格式
		DateTimeFormatter timePattern = DateTimeFormatter.ofPattern("HH:mm:ss");
		//日期格式
		DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		//完整日期时间格式
		DateTimeFormatter dateTimePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timePattern));
		javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(datePattern));
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimePattern));
		javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timePattern));
		javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(datePattern));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimePattern));
		mapper.registerModule(javaTimeModule);

	}

	public static JsonNode parseJson(String json) {
		JsonNode node = null;
		try {
			node = mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}
	
	public static ObjectNode parseToObjectNode(String json) {
		ObjectNode node = null;
		try {
			node = (ObjectNode) mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}
	public static ArrayNode parseToArrayNode(String json) {
		ArrayNode node = null;
		try {
			node = (ArrayNode) mapper.readTree(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return node;
	}
	
	public static String createJson(Object obj){
		String jsonStr = null;
		try {
			jsonStr = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
	
	public static <T> T parseAsSimpleObj(String jsonStr, Class<T> objClass){
		T t = null;
		try {
			t = mapper.readValue(jsonStr, objClass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static <T> T parseAsComplexObj(String jsonStr, TypeReference<T> typeReference){
		T t=null;
		try {
			t = mapper.readValue(jsonStr, typeReference);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
}
