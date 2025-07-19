package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockPage.
 *
 * Page routes use directory structure from /pages directory with special handling
 * for the landing page which maps to root path.
 *
 * Written by Claude Sonnet 4
 */
class WebBlockPageRouteDescriptor(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    private val systemPaths = listOf("api", "page-api", "static")

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

        // Ensure class has @WebBlockPage annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.PAGE) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockPage",
            )
        }

        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val returnPath = if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages

        systemPaths.forEach { systemPath ->
            if (returnPath.startsWith(systemPath)) {
                throw IllegalArgumentException(
                    "Page path cannot start with '$systemPath'. Page path: '$returnPath'",
                )
            }
        }

        return returnPath
    }

    override fun getTypeName() = "WebBlockPageRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}
