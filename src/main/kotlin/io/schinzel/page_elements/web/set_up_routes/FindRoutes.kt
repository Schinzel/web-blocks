package io.schinzel.page_elements.web.set_up_routes

import io.schinzel.page_elements.web.response_handlers.IResponseHandler
import io.schinzel.page_elements.web.response_handler_mapping.ResponseHandlerMapping
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder


fun findRoutes(webRootPackage: String): List<ResponseHandlerMapping> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(webRootPackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IResponseHandler::class.java)
        // Filter only classes in the base package and subpackages. This is needed because
        // Reflections will scan all classes in the classpath by default.
        .filter { clazz ->
            clazz.packageName.startsWith(webRootPackage) && !clazz.isInterface
        }
        .map {
            ResponseHandlerMapping(it.kotlin)
        }
        .toList()
}

