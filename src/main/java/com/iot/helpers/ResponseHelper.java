package com.iot.helpers;

import io.vertx.core.json.JsonObject;

public class ResponseHelper {
	private ResponseHelper() {
		// Hide public constructor
	}

	public static JsonObject buildSuccess(final int status, final JsonObject data) {
		return buildResponse(true, status, data);
	}

	public static JsonObject buildError(final int status, final String error, final String details) {
		final JsonObject data = new JsonObject();
		data.put("error", error);
		data.put("details", details);
		return buildResponse(false, status, data);
	}

	private static JsonObject buildResponse(final boolean success, final int status, final JsonObject data) {
		final JsonObject jsonObj = new JsonObject();
		jsonObj.put("success", success);
		jsonObj.put("status", status);
		jsonObj.put("data", data);

		return jsonObj;
	}
}
