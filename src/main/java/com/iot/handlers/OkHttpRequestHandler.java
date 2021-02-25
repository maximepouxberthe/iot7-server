package com.iot.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;

public class OkHttpRequestHandler {
	protected String url;
	protected String method;
	protected RequestBody body;
	protected Map<String, String> headers = new HashMap<>();

	public final OkHttpRequestHandler withUrl(final String url) {
		this.url = url;
		return this;
	}

	public final OkHttpRequestHandler withMethod(final String method) {
		this.method = method;
		return this;
	}

	public final OkHttpRequestHandler withRequestBody(final RequestBody body) {
		this.body = body;
		return this;
	}

	public final OkHttpRequestHandler withHeaders(final Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public final Request build() {
		final Builder builder = new Request.Builder().url(url).method(method, body);
		for (final Entry<String, String> header : headers.entrySet()) {
			builder.addHeader(header.getKey(), header.getValue());
		}
		return builder.build();
	}
}
