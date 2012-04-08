package jsjunit.service;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class ResultService {
	
	public <T> T createResult(String str, Class<T> entityClass) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			T value = mapper.readValue(str, entityClass);
			return value;
		} catch (JsonParseException e) {
			throw new IllegalStateException(e);
		} catch (JsonMappingException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}
}
