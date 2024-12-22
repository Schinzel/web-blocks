package io.schinzel.page_elements.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.route.Route
import io.schinzel.page_elements.route.log.ILogger
import io.schinzel.page_elements.route.log.PrettyConsoleLogger
import io.schinzel.page_elements.route_handler.createRouteHandler

fun setUpRoutes(
    pagePackage: String,
    apiPackage: String,
    localTimezone: String = "Europe/Stockholm",
    logger: ILogger = PrettyConsoleLogger(),
){
    val javalin = Javalin.create { config ->
        // Serve static files at /static/*
        config.staticFiles.add {
            it.directory = "/static"
            it.location = Location.CLASSPATH
            it.hostedPath = "/static"
        }
    }
    // Find all page routes
    val pageRoutes = findRoutes(pagePackage)
    // Find all api routes
    val apiRoutes = findRoutes(apiPackage)
    // Add all routes to Javalin
    (pageRoutes + apiRoutes).forEach { route: Route ->
        // Print route
        route.toString().println()
        // Create handler
        val handler = createRouteHandler(route, localTimezone, logger)
        // Register both GET and POST handlers for the same path
        javalin.get(route.getPath(), handler)
        javalin.post(route.getPath(), handler)
        // Check if route has arguments
        val hasArguments = route.parameters.isNotEmpty()
        // If has arguments
        if (hasArguments) {
            // Create path with parameters
            val pathWithParams = route.parameters.fold(route.getPath()) { path, param ->
                "$path/{${param.name}}"
            }
            // Register both GET and POST handlers for the same path
            javalin.get(pathWithParams, handler)
            javalin.post(pathWithParams, handler)
        }

    }
    // Start server
    javalin.start(5555)

}