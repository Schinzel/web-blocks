package io.schinzel.web_blocks.web.routes.route_descriptors

import io.schinzel.web_blocks.web.routes.IRoute
import kotlin.reflect.KClass

/**
 * The purpose of this class is to store and manage route descriptors for
 * annotation-based route discovery system.
 *
 * Simplified to support only the new annotation-based routes.
 *
 * Written by Claude Sonnet 4
 */
object RouteDescriptorRegistry {
    private val routeDescriptors = mutableListOf<IRouteDescriptor<IRoute>>()

    /**
     * Register a descriptor for annotation-based routes
     */
    fun registerAnnotation(descriptor: IRouteDescriptor<IRoute>): RouteDescriptorRegistry {
        routeDescriptors.add(descriptor)
        return this
    }

    /**
     * Get route descriptor for annotation-based route class
     */
    fun getRouteDescriptor(clazz: KClass<out IRoute>): IRouteDescriptor<IRoute> =
        routeDescriptors.find { descriptor ->
            clazz.annotations.any { it.annotationClass == descriptor.annotation }
        } ?: throw RuntimeException("No route descriptor found for $clazz")

    /**
     * Clear all registered route descriptors (primarily for test cleanup)
     */
    fun clear() {
        routeDescriptors.clear()
    }
}
