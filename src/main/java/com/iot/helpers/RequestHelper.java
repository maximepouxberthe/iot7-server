package com.iot.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Triplet;

import com.fasterxml.jackson.databind.JsonNode;
import com.iot.constants.OkHttpConstants;

public class RequestHelper {

	private static final String OBJECT_IP = "127.0.0.1";
	private static final String OBJECT_PORT = "8080";

	private RequestHelper() {
		// Hide public constructor
	}

	private static float getFreeSpaceArduino() {
		try {
			final JsonNode response = JacksonJsonHelper
					.getJsonNodeFromString(OkHttpRequestHelper.getBodyFromResponse(OkHttpRequestHelper.executeRequest(
							OkHttpRequestHelper.createRequest().withUrl(OBJECT_IP + ":" + OBJECT_PORT + "/storage")
									.withMethod(OkHttpConstants.METHOD_GET).withRequestBody(OkHttpRequestHelper
											.createRequestMultipartBody().withMultipartBodyMediaType().build())
									.build())));
			return response.get("size").floatValue();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void addMusicToArduino(final String filename) {
		final File file = new File("data/" + filename);

		if (getFreeSpaceArduino() > file.length()) {

			final Triplet<String, String, File> triplet = new Triplet<>("file", filename, file);
			final List<Triplet<String, String, File>> list = new ArrayList<>();
			list.add(triplet);

			try {
				OkHttpRequestHelper.executeRequest(OkHttpRequestHelper
						.createRequest().withUrl(OBJECT_IP + ":" + OBJECT_PORT + "/music")
						.withMethod(OkHttpConstants.METHOD_POST).withRequestBody(OkHttpRequestHelper
								.createRequestMultipartBody().withFiles(list).withMultipartBodyMediaType().build())
						.build());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}
}
