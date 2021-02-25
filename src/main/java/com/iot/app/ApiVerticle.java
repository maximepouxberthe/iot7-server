package com.iot.app;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.iot.helpers.RequestHelper;
import com.iot.helpers.ResponseHelper;
import com.iot.services.MusicService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiVerticle extends AbstractVerticle {

	private final MusicService musicService = new MusicService();

	private static final Logger LOGGER = LogManager.getLogger(ApiVerticle.class);

	@Override
	public void start() throws Exception {

		final Router router = Router.router(vertx);

		// create the body for all routes
		router.route().handler(BodyHandler.create());

		final Route route = router.route(HttpMethod.POST, "/api/v1/music");
		route.handler(this::addMusic);

		router.route(HttpMethod.GET, "/api/v1/music").handler(this::listMusics);
		router.route(HttpMethod.POST, "/api/v1/music/:id").handler(this::addMusicToArduino);

		vertx.createHttpServer().requestHandler(router).listen(8080);
		LOGGER.info("The application is listening on 8080 port");
	}

	@Override
	public void stop() throws Exception {
		// Nothing to do yet
	}

	public void logRequest(final RoutingContext context) {
		LOGGER.info(String.format("Received a %s request", context.normalizedPath()));
	}

	public void addMusic(final RoutingContext context) {
		logRequest(context);
		final Set<FileUpload> uploads = context.fileUploads();
		if (uploads == null || uploads.size() != 1) {
			final JsonObject jsonResponse = ResponseHelper.buildError(400, "Invalid Parameter",
					"Expected 1 binary attached to the request");
			context.response().setStatusCode(200).putHeader("content-type", "application/json")
					.end(Json.encodePrettily(jsonResponse));
			LOGGER.warn(String.format("%s request rejected, 1 attached file expected, got %s", context.normalizedPath(),
					uploads == null ? 0 : uploads.size()));
			return;
		}
		musicService.addMusic(context, (FileUpload) uploads.toArray()[0]);
	}

	public void listMusics(final RoutingContext context) {
		logRequest(context);
		musicService.listMusics(context);
	}

	public void addMusicToArduino(final RoutingContext context) {
		final String filename = context.request().getParam("id");
		RequestHelper.addMusicToArduino(filename);
		context.response().setStatusCode(200).putHeader("content-type", "application/json")
				.end(Json.encodePrettily(ResponseHelper.buildSuccess(200, new JsonObject())));
	}
}
