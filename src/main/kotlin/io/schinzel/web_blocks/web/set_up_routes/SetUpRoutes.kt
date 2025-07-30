package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.web_blocks.web.WebAppConfig

/**
 * The purpose of this function is to set up Javalin routes using a clean extension function chain.
 *
 * Written by Claude Sonnet 4
 */
fun setUpRoutes(webAppConfig: WebAppConfig): Javalin {
    // Find all routes and capture them for both registration and overview page
    val routeMappings = findRoutes(webAppConfig.webRootPackage)

    return Javalin
        .create { config ->
            // Serve static files at /static/*
            config.staticFiles.add {
                it.directory = "/static"
                it.location = Location.CLASSPATH
                it.hostedPath = "/static"
            }
        }.setupBeforeHandler(webAppConfig)
        .setupAfterHandler(webAppConfig)
        .setupFrameworkRoutes(routeMappings)
        .setupUserRoutes(webAppConfig, routeMappings)
}
