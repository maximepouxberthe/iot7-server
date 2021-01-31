package com.iot.services;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iot.helpers.ResponseHelper;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;

public class MusicService {

	private static final Logger LOGGER = LogManager.getLogger(MusicService.class);

	public void addMusic(final RoutingContext context, final FileUpload music) {
		LOGGER.info(String.format("Adding %s music to the server", music.fileName()));
		JsonObject jsonResponse;
		int status = 0;

		if (!"audio/mpeg".equals(music.contentType())) {
			jsonResponse = ResponseHelper.buildError(415, "Wrong mimetype",
					String.format("%s is a %s. This application only handles audio/mpeg files", music.fileName(),
							music.contentType()));
			LOGGER.warn(String.format("%s request rejected, file %s of %s mimetype not handled",
					context.normalizedPath(), music.fileName(), music.contentType()));
			status = 415;
		} else {

			final File savedMusic = new File("data/" + music.fileName());
			final File tempMusic = new File(music.uploadedFileName());
			if (savedMusic.exists()) {
				jsonResponse = ResponseHelper.buildError(409, "File already exists",
						String.format("There is already a file named %s", music.fileName()));
				LOGGER.warn(String.format("%s request rejected, file with same name already exists: %s",
						context.normalizedPath(), music.fileName()));
				status = 409;
			} else {
				try {
					com.google.common.io.Files.copy(tempMusic, savedMusic);
					final JsonObject data = new JsonObject();
					data.put("file", music.fileName());
					jsonResponse = ResponseHelper.buildSuccess(201, data);
					status = 201;
				} catch (final IOException e) {
					jsonResponse = ResponseHelper.buildError(500, "I/O Exception",
							String.format("Failed to save '%s' on server, %s", music.fileName(), e.getMessage()));
					LOGGER.warn(String.format("%s request rejected, failed to save '%s' on server, %s",
							context.normalizedPath(), music.fileName(), e.getMessage()));
					status = 500;
				}
			}
		}

		context.response().setStatusCode(status).putHeader("content-type", "application/json")
				.end(Json.encodePrettily(jsonResponse));
		LOGGER.info(String.format("%s request ended with %s status", context.normalizedPath(), status));
	}

	public void listMusics(final RoutingContext context) {
		LOGGER.info("Listing all musics available on the server");
		final File directory = new File("data");
		final JsonObject data = new JsonObject();
		final JsonArray files = new JsonArray();
		for (final File file : directory.listFiles()) {
			try {
				if ("audio/mpeg".equals(java.nio.file.Files.probeContentType(file.toPath()))) {
					final JsonObject jsonFile = new JsonObject();
					jsonFile.put("name", file.getName());
					files.add(jsonFile);
				}
			} catch (final IOException e) {
				context.response().setStatusCode(500).putHeader("content-type", "application/json")
						.end(Json.encodePrettily(ResponseHelper.buildError(500, "I/O Exception", String
								.format("Failed to read file '%s' on server, %s", file.getName(), e.getMessage()))));
				LOGGER.info(String.format("%s request ended with %s status", context.normalizedPath(), 500));
			}
		}
		data.put("files", files);
		context.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(Json.encodePrettily(ResponseHelper.buildSuccess(200, data)));
		LOGGER.info(String.format("%s request ended with %s status", context.normalizedPath(), 200));
	}
}
