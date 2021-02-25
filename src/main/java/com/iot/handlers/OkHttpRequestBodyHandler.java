package com.iot.handlers;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public abstract class OkHttpRequestBodyHandler {
	protected MediaType mediaType;
	protected Map<String, String> formDataParts;

	public final OkHttpRequestBodyHandler withFormDataParts(final Map<String, String> formDataParts) {
		this.formDataParts = formDataParts;
		return this;
	}

	public final OkHttpRequestBodyHandler withMultipartBodyMediaType() {
		this.mediaType = MultipartBody.FORM;
		return this;
	}

	public abstract RequestBody build();
}
