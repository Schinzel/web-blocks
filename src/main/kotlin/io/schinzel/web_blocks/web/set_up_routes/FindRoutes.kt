package io.schinzel.web_blocks.web.set_up_routes

import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder


fun findRoutes(webRootPackage: String): List<RouteMapping> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(webRootPackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IRoute::class.java)
        // Filter only classes in the base package and subpackages. This is needed because
        // Reflections will scan all classes in the classpath by default.
        .filter { clazz ->
            clazz.packageName.startsWith(webRootPackage) && !clazz.isInterface
        }
        .map {
            RouteMapping(it.kotlin)
        }
        .toList()
}

