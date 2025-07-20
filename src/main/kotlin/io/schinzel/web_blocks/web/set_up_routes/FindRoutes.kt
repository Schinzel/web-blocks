package io.schinzel.web_blocks.web.set_up_routes

import io.schinzel.web_blocks.web.route_mapping.RouteMapping
import io.schinzel.web_blocks.web.routes.IApiRoute
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.NewWebBlockApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.RouteDescriptorPage
import io.schinzel.web_blocks.web.routes.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.RouteTypeEnum
import io.schinzel.web_blocks.web.routes.WebBlockApiRouteDescriptor
import io.schinzel.web_blocks.web.routes.WebBlockRouteDescriptor
import io.schinzel.web_blocks.web.routes.annotations.Api
import io.schinzel.web_blocks.web.routes.annotations.Page
import io.schinzel.web_blocks.web.routes.annotations.WebBlockApi
import org.reflections.Reflections
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import io.schinzel.web_blocks.web.routes.annotations.WebBlock as WebBlockAnnotation

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
            RouteDescriptorPage(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.API,
            WebBlockApiRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.WEB_BLOCK,
            WebBlockRouteDescriptor(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.WEB_BLOCK_API,
            NewWebBlockApiRouteDescriptor(endpointPackage),
        )
    }

    /**
     * Get all annotation-based route classes
     */
    fun getAnnotationBasedRoutes(): List<KClass<out IWebBlockRoute<*>>> {
        val routes = mutableListOf<KClass<out IWebBlockRoute<*>>>()

        // Find classes annotated with WebBlock route annotations
        routes.addAll(findAnnotatedRoutes<Page>())
        routes.addAll(findAnnotatedRoutes<Api>())
        routes.addAll(findAnnotatedRoutes<WebBlockAnnotation>())
        routes.addAll(findAnnotatedRoutes<WebBlockApi>())

        return routes
    }

    /**
     * Find classes annotated with a specific route annotation
     */
    private inline fun <reified T : Annotation> findAnnotatedRoutes(): List<KClass<out IWebBlockRoute<*>>> =
        reflections
            .getTypesAnnotatedWith(T::class.java)
            .filter { clazz ->
                // Validate that annotated class implements IWebBlockRoute
                IWebBlockRoute::class.java.isAssignableFrom(clazz)
            }.map { clazz ->
                @Suppress("UNCHECKED_CAST")
                clazz.kotlin as KClass<out IWebBlockRoute<*>>
            }.also { routes ->
                // Validate each route
                routes.forEach { route ->
                    RouteAnnotationUtil.validateRouteAnnotation(route)

                    // Then validate interface requirements
                    when {
                        route.hasAnnotation<Page>() && !route.isSubclassOf(IHtmlRoute::class) ->
                            throw IllegalStateException(
                                "@Page annotated class ${route.simpleName} must implement IHtmlRoute",
                            )

                        route.hasAnnotation<Api>() && !route.isSubclassOf(IApiRoute::class) ->
                            throw IllegalStateException(
                                "@Api annotated class ${route.simpleName} must implement IApiRoute",
                            )

                        route.hasAnnotation<WebBlockAnnotation>() && !route.isSubclassOf(IHtmlRoute::class) ->
                            throw IllegalStateException(
                                "@WebBlock annotated class ${route.simpleName} must implement IHtmlRoute",
                            )

                        route.hasAnnotation<WebBlockApi>() && !route.isSubclassOf(IApiRoute::class) ->
                            throw IllegalStateException(
                                "@WebBlockApi annotated class ${route.simpleName} must implement IApiRoute",
                            )
                    }
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
        @Suppress("UNCHECKED_CAST")
        RouteMapping(routeClass as KClass<out IRoute>)
    }
}
