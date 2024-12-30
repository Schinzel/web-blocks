package io.schinzel.web_app_engine.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.web_app_engine.WebAppConfig
import io.schinzel.web_app_engine.request_handler.RequestHandler
import io.schinzel.web_app_engine.response_handler_mapping.ResponseHandlerMapping
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun setUpRoutes(webAppConfig: WebAppConfig): Javalin? {
    val javalin = Javalin.create { config ->
        // Serve static files at /static/*
        config.staticFiles.add {
            it.directory = "/static"
            it.location = Location.CLASSPATH
            it.hostedPath = "/static"
        }
    }
    // Find all routes and add them Javalin
    findRoutes(webAppConfig.endpointPackage).forEach { responseHandlerMapping: ResponseHandlerMapping ->
        // Create request handler
        val requestHandler = RequestHandler(responseHandlerMapping, webAppConfig)
            .getHandler()
        // Register both GET and POST handlers for the same path
        javalin.getAndPost(responseHandlerMapping.path, requestHandler)
    }
    javalin.get("ping") { ctx ->
        ctx.result("pong " + Instant.now().toIsoString())
    }
    javalin.start(webAppConfig.port)
    return javalin
}


private fun Instant.toIsoString(): String? {
    val zonedDateTime = ZonedDateTime.ofInstant(this, ZoneId.of("Europe/Stockholm"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return zonedDateTime.format(formatter)
}

private fun Javalin.getAndPost(path: String, handler: (ctx: io.javalin.http.Context) -> Unit) {
    this.get(path, handler)
    this.post(path, handler)
}