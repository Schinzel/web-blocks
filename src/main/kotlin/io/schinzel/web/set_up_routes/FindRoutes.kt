package io.schinzel.web.set_up_routes

import io.schinzel.web.response_handlers.IResponseHandler
import io.schinzel.web.response_handler_mapping.ResponseHandlerMapping
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder


fun findRoutes(endpointPackage: String): List<ResponseHandlerMapping> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(endpointPackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IResponseHandler::class.java)
        // Filter only classes in the base package and subpackages. This is needed because
        // Reflections will scan all classes in the classpath by default.
        .filter { clazz ->
            clazz.packageName.startsWith(endpointPackage) && !clazz.isInterface
        }
        .map {
            ResponseHandlerMapping(it.kotlin)
        }
        .toList()
}

