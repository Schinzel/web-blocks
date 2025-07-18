package io.schinzel.web_blocks.web.routes

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
    private val annotationDescriptors =
        mutableMapOf<RouteTypeEnum, IRouteDescriptor<IRoute>>()

    /**
     * Register a descriptor for annotation-based routes
     */
    fun registerAnnotation(
        routeType: RouteTypeEnum,
        descriptor: IRouteDescriptor<IRoute>,
    ) {
        annotationDescriptors[routeType] = descriptor
    }

    /**
     * Get route descriptor for annotation-based route class
     */
    @Suppress("UNCHECKED_CAST")
    fun getRouteDescriptor(clazz: KClass<out IRoute>): IRouteDescriptor<IRoute> {
        // Check if it's a IWebBlockRoute with annotation
        if (IWebBlockRoute::class.java.isAssignableFrom(clazz.java)) {
            val routeType = RouteAnnotationUtil.detectRouteType(clazz)

            if (routeType == RouteTypeEnum.UNKNOWN) {
                throw IllegalArgumentException(
                    "Route class ${clazz.simpleName} has no WebBlock annotation",
                )
            }

            return annotationDescriptors[routeType]
                ?: throw IllegalArgumentException(
                    "No descriptor registered for route type $routeType",
                )
        }

        // For legacy interface-based routes, use the old system
        throw IllegalArgumentException(
            "Legacy interface-based routes are not supported in this version. " +
                "Please use IWebBlockRoute with annotations instead.",
        )
    }
}
