package io.schinzel.web_app_engine.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.web_app_engine.route_handler.RequestHandler
import io.schinzel.web_app_engine.route_handler.log.ILogger
import io.schinzel.web_app_engine.route_handler.log.PrettyConsoleLogger
import io.schinzel.web_app_engine.route_mapping.RouteMapping

fun setUpRoutes(
    webPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
) {
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
        // Create handler
        val handler = RequestHandler(
            routeMapping = routeMapping,
            localTimezone = localTimezone,
            logger = logger
        ).handle()
        // Register both GET and POST handlers for the same path
        javalin.getAndPost(routeMapping.path, handler)
        // Check if route has arguments
        val hasArguments = routeMapping.parameters.isNotEmpty()
        // If has arguments
        if (hasArguments) {
            // Create path with parameters
            val pathWithParams = routeMapping.parameters.fold(routeMapping.path) { path, param ->
                "$path/{${param.name}}"
            }
            // Register both GET and POST handlers for the same path
            javalin.getAndPost(pathWithParams, handler)
        }
    }
    // Start server
    javalin.start(5555)
}


private fun Javalin.getAndPost(path: String, handler: (ctx: io.javalin.http.Context) -> Unit) {
    this.get(path, handler)
    this.post(path, handler)
}