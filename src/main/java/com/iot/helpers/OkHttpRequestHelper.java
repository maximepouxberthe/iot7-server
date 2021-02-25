package com.iot.helpers;

import java.io.IOException;

import com.iot.handlers.OkHttpRequestHandler;
import com.iot.handlers.OkHttpRequestMultipartBodyHandler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequestHelper {

	private OkHttpRequestHelper() {
		// Hide public constructor
	}

	public static Response executeRequest(final Request request) throws IOException {
		final OkHttpClient client = new OkHttpClient();
		return client.newCall(request).execute();
	}

	public static String getBodyFromResponse(final Response response) throws IOException {
		final String bodyResp = response.body().string();
		response.body().close();
		return bodyResp;
	}

	public static int getHttpCodeFromResponse(final Response response) {
		return response.code();
	}

	public static OkHttpRequestHandler createRequest() {
		return new OkHttpRequestHandler();
	}

	public static OkHttpRequestMultipartBodyHandler createRequestMultipartBody() {
		return new OkHttpRequestMultipartBodyHandler();
	}

	public static byte[] getBodyFromResponseAsBytes(final Response response) throws IOException {
		final byte[] bodyResp = response.body().bytes();
		response.body().close();
		return bodyResp;
	}
}
