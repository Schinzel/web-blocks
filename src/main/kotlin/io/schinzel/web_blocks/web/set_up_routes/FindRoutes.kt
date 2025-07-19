package io.schinzel.web_blocks.web.set_up_routes

import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.RouteTypeEnum
import io.schinzel.web_blocks.web.routes.WebBlockApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.WebBlockPageApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.WebBlockPageRouteDescriptor
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi
import org.reflections.Reflections
import kotlin.reflect.KClass

/**
 * The purpose of this class is to discover and register annotation-based routes
 * in the WebBlocks framework.
 *
 * Simplified to support only the new annotation-based system.
 *
 * Written by Claude Sonnet 4
 */
class FindRoutes(
    private val endpointPackage: String,
) {
    private val reflections = Reflections(endpointPackage)

    /**
     * Discover and register all annotation-based routes
     */
    fun registerRoutes() {
        // Register descriptors for annotation-based routes
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE,
            WebBlockPageRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.API,
            WebBlockApiRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE_API,
            WebBlockPageApiRouteDescriptor(endpointPackage),
        )
    }

    /**
     * Get all annotation-based route classes
     */
    fun getAnnotationBasedRoutes(): List<KClass<out IWebBlockRoute>> {
        val routes = mutableListOf<KClass<out IWebBlockRoute>>()

        // Find classes annotated with WebBlock route annotations
        routes.addAll(findAnnotatedRoutes<Page>())
        routes.addAll(findAnnotatedRoutes<Api>())
        routes.addAll(findAnnotatedRoutes<WebBlockPageApi>())

        return routes
    }

    /**
     * Find classes annotated with a specific route annotation
     */
    private inline fun <reified T : Annotation> findAnnotatedRoutes(): List<KClass<out IWebBlockRoute>> =
        reflections
            .getTypesAnnotatedWith(T::class.java)
            .filter { clazz ->
                // Validate that annotated class implements IWebBlockRoute
                IWebBlockRoute::class.java.isAssignableFrom(clazz)
            }.map { clazz ->
                @Suppress("UNCHECKED_CAST")
                clazz.kotlin as KClass<out IWebBlockRoute>
            }.also { routes ->
                // Validate each route
                routes.forEach { route ->
                    RouteAnnotationUtil.validateRouteAnnotation(route)
                }
            }
}

fun findRoutes(webRootPackage: String): List<RouteMapping> {
    val findRoutes = FindRoutes(webRootPackage)

    // Register route descriptors
    findRoutes.registerRoutes()

    // Get all annotation-based routes
    val annotationBasedRoutes = findRoutes.getAnnotationBasedRoutes()

    return annotationBasedRoutes.map { routeClass ->
        RouteMapping(routeClass)
    }
}
