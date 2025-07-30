package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.request_handler.RequestHandler
import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes_overview.RoutesJsonGenerator
import io.schinzel.web_blocks.web.routes_overview.RoutesOverviewPageGenerator
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun setUpRoutes(webAppConfig: WebAppConfig): Javalin {
    val javalin =
        Javalin.create { config ->
            // Serve static files at /static/*
            config.staticFiles.add {
                it.directory = "/static"
                it.location = Location.CLASSPATH
                it.hostedPath = "/static"
            }
        }
    // Find all routes and capture them for both registration and overview page
    val routeMappings = findRoutes(webAppConfig.webRootPackage)

    // Register framework routes before user routes to ensure precedence
    val routesOverviewGenerator = RoutesOverviewPageGenerator()
    javalin.get("/web-blocks/routes") { ctx ->
        val html = routesOverviewGenerator.generateHtml(routeMappings)
        ctx.html(html)
    }

    // JSON version for AI/tooling
    javalin.get("/web-blocks/routes-json") { ctx ->
        val json = RoutesJsonGenerator(routeMappings).generateJson()
        ctx.json(json)
    }

    // A simple endpoint to check if is up and running
    javalin.get("/web-blocks/ping") { ctx ->
        ctx.result("pong " + Instant.now().toIsoString())
    }

    // Register user routes
    routeMappings.forEach { routeMapping: RouteMapping ->
        if (webAppConfig.printStartupMessages) {
            // Print the route
            routeMapping.println()
        }
        // Create request handler
        val requestHandler =
            RequestHandler(routeMapping, webAppConfig)
                .getHandler()
        // Register both GET and POST handlers for the same path
        javalin.getAndPost(routeMapping.routePath, requestHandler)
    }
    return javalin
}

private fun Instant.toIsoString(): String? {
    val zonedDateTime = ZonedDateTime.ofInstant(this, ZoneId.of("UTC"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return zonedDateTime.format(formatter)
}

private fun Javalin.getAndPost(
    path: String,
    handler: (ctx: io.javalin.http.Context) -> Unit,
) {
    this.get(path, handler)
    this.post(path, handler)
}
