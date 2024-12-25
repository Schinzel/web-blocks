package io.schinzel.web_app_engine.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_app_engine.route_handler.RequestHandler
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_mapping.RouteMapping
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun setUpRoutes(
    webPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
): Javalin? {
    val javalin = Javalin.create { config ->
        // Serve static files at /static/*
        config.staticFiles.add {
            it.directory = "/static"
            it.location = Location.CLASSPATH
            it.hostedPath = "/static"
        }
    }
    // Find all routes and add them Javalin
    findRoutes(webPackage).forEach { routeMapping: RouteMapping ->
        routeMapping.println()
        // Create handler
        val handler = RequestHandler(
            routeMapping = routeMapping,
            localTimezone = localTimezone,
            logger = logger
        ).handle()
        // Register both GET and POST handlers for the same path
        javalin.getAndPost(routeMapping.path, handler)
    }
    javalin.get("ping") { ctx ->
        ctx.result("pong " + Instant.now().toIsoString())
    }

        javalin.start(5555)

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