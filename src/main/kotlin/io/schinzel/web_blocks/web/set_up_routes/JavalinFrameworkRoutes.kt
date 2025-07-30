package io.schinzel.web_blocks.web.set_up_routes

import io.javalin.Javalin
import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes_overview.RoutesJsonGenerator
import io.schinzel.web_blocks.web.routes_overview.RoutesOverviewPageGenerator
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * The purpose of this class is to provide framework routes extension for Javalin setup.
 * Registers web-blocks internal routes like /web-blocks/routes, /web-blocks/ping, etc.
 *
 * Written by Claude Sonnet 4
 */
fun Javalin.setupFrameworkRoutes(routeMappings: List<RouteMapping>): Javalin {
    this.get("/web-blocks/routes") { ctx ->
        val html = RoutesOverviewPageGenerator().generateHtml(routeMappings)
        ctx.html(html)
    }

    // JSON version for AI/tooling
    this.get("/web-blocks/routes-json") { ctx ->
        val json = RoutesJsonGenerator(routeMappings).generateJson()
        ctx.json(json)
    }

    // A simple endpoint to check if is up and running
    this.get("/web-blocks/ping") { ctx ->
        ctx.result("pong " + Instant.now().toIsoString())
    }

    return this
}

private fun Instant.toIsoString(): String? {
    val zonedDateTime = ZonedDateTime.ofInstant(this, ZoneId.of("UTC"))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return zonedDateTime.format(formatter)
}
