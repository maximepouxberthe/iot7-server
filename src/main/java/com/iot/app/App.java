package com.iot.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.vertx.core.Vertx;

public class App {

	private static final Logger LOGGER = LogManager.getLogger(App.class);

	public static void main(final String[] args) {
		LOGGER.info("Deploying the application");
		final Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new ApiVerticle());
	}
}