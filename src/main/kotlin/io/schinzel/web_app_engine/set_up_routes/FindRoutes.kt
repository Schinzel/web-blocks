package io.schinzel.web_app_engine.set_up_routes

import io.schinzel.web_app_engine.IRequestProcessor
import io.schinzel.web_app_engine.route_mapping.RouteMapping
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder


fun findRoutes(basePackage: String): List<RouteMapping> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IRequestProcessor::class.java)
        // Filter only classes in the base package and subpackages. This is needed because
        // Reflections will scan all classes in the classpath by default.
        .filter { clazz ->
            clazz.packageName.startsWith(basePackage) && !clazz.isInterface
        }
        .map {
            RouteMapping(basePackage, it.kotlin)
        }
        .toList()
}

