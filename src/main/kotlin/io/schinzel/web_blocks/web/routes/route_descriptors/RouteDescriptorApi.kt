package io.schinzel.web_blocks.web.routes.route_descriptors

import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.annotations.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum
import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockPageApi.
 *
 * Page API routes use directory structure from /pages directory plus the class name
 * with kebab-case conversion and "Route" suffix removal, prefixed with "page-api".
 *
 * Written by Claude Sonnet 4
 */
class RouteDescriptorApi(
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
        val webBlockRouteClass = clazz as KClass<out IWebBlockRoute<*>>

        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(webBlockRouteClass)

        // Ensure class has @WebBlockPageApi annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.PAGE_API) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockPageApi",
            )
        }

        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase =
            RouteUtil
                .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebBlockPageApiRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}
