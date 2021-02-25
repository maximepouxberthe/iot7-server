package com.iot.helpers;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonHelper {

	private JacksonJsonHelper() {
		// Hide public constructor
	}

	public static JsonNode getJsonNodeFromString(final String string) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(string);
	}
}
