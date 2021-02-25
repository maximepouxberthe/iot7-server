package com.iot.handlers;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.javatuples.Triplet;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.RequestBody;

public class OkHttpRequestMultipartBodyHandler extends OkHttpRequestBodyHandler {

	List<Triplet<String, String, File>> files;

	public final OkHttpRequestBodyHandler withFiles(final List<Triplet<String, String, File>> files) {
		this.files = files;
		return this;
	}

	@Override
	public RequestBody build() {
		final Builder builder = new MultipartBody.Builder().setType(mediaType);
		for (final Entry<String, String> formDataPart : formDataParts.entrySet()) {
			builder.addFormDataPart(formDataPart.getKey(), formDataPart.getValue());
		}
		for (final Triplet<String, String, File> file : files) {
			builder.addFormDataPart(file.getValue0(), file.getValue1(),
					RequestBody.create(MediaType.parse("audio/mpeg"), file.getValue2()));
		}
		return builder.build();
	}

}
