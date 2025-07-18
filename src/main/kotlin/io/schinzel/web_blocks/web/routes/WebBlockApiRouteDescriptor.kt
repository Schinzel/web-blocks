package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockApi.
 *
 * API routes use directory structure from /api directory plus the class name
 * with kebab-case conversion and "Route" suffix removal.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockApiRouteDescriptor(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    override fun getRoutePath(clazz: KClass<out IRoute>): String {
        // Ensure class implements IWebBlockRoute
        if (!IWebBlockRoute::class.java.isAssignableFrom(clazz.java)) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} must implement IWebBlockRoute",
            )
        }

        @Suppress("UNCHECKED_CAST")
        val webBlockRouteClass = clazz as KClass<out IWebBlockRoute>

        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(webBlockRouteClass)

        // Ensure class has @WebBlockApi annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.API) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockApi",
            )
        }

        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val classNameKebabCase =
            RouteUtil
                .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName() = "WebBlockApiRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}
