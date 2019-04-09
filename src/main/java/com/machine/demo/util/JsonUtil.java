package com.machine.demo.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

	private static ObjectMapper objectMapper = null;
	

	static {
		objectMapper = new ObjectMapper();
		objectMapper.setVisibility(PropertyAccessor.GETTER, Visibility.ANY);
	}

	public static <T> String getJson(T data) {
		try {
			if (data != null)
				return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			log.warn("Error occured in converting to json : ", e);
		}
		return null;
	}

	public static <T> T convertToPojo(String json, Class<T> t) throws Exception {
		return objectMapper.readValue(json, t);
	}
	
	public static <T> T tryConvertToPojo(String json, Class<T> t)  {
		try {
			return convertToPojo(json, t);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> convertToListPojo(String json, Class<T> t)  {
		try {
			return objectMapper.readValue(json, new TypeReference<List<T>>() {
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void writeValue(Writer w, Object data)  {
		try {
			objectMapper.writeValue(w, data);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
