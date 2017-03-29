package org.bib.tbase.web.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageResolver;
import org.springframework.webflow.execution.RequestContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring WebFlow utility methods
 */
public class FlowUtil {
// Constructors

// Public Methods
	public static MessageResolver err(String source, String defaultMessage) {
		return err(source, "", defaultMessage);
	}
	
	public static MessageResolver err(String source, String code, String defaultMessage) {
		return new MessageBuilder().error().source(source).code(code).defaultText(defaultMessage).build();
	}
	
	/**
	 * Read the JSON request body from the argument RequestContext and parse it into an Object of argument valueType.
	 * 
	 * @param context The Flow RequestContext instance, never null
	 * @param valueType The type to return, never null
	 * @return An instance of T, never null
	 * @throws IOException An error occurs while reading the data
	 */
	public static <T> T readJson(RequestContext context, Class<T> valueType) throws IOException {
		HttpServletRequest servReq = (HttpServletRequest) context.getExternalContext().getNativeRequest();
		
		ObjectMapper objMapper = new ObjectMapper();
		
		T obj = objMapper.readValue(servReq.getInputStream(), valueType);
		
		return obj;
	}

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(FlowUtil.class);
}
