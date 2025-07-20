package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlockApi.
 *
 * WebBlock API routes generate paths with /web-block-api/ prefix and follow directory structure
 * from the source file location. Class names are converted from PascalCase to kebab-case
 * with "Route" suffix removed if present.
 *
 * Path generation examples:
 * - /pages/profile/blocks/name_block/UpdateNameRoute.kt → /web-block-api/profile/blocks/name-block/update-name
 * - /pages/dashboard/widgets/weather/RefreshWeatherRoute.kt → /web-block-api/dashboard/widgets/weather/refresh-weather
 *
 * Written by Claude Sonnet 4
 */
class NewWebBlockApiRouteDescriptor(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    private val systemPaths = listOf("api", "page-api", "web-block", "web-block-api", "static")

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

        // Ensure class has @WebBlockApi annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.WEB_BLOCK_API) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlockApi",
            )
        }

        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = RouteUtil.removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        val returnPath = "web-block-api/$pagePathWithoutPages/$classNameKebabCase"

        // Validate against system paths (excluding web-block-api since it's our prefix)
        val pathAfterPrefix = returnPath.removePrefix("web-block-api/")
        systemPaths.filter { it != "web-block-api" }.forEach { systemPath ->
            if (pathAfterPrefix.startsWith(systemPath)) {
                throw IllegalArgumentException(
                    "WebBlock API path cannot start with '$systemPath' after web-block-api/ prefix. Path: '$returnPath'",
                )
            }
        }

        return returnPath
    }

    override fun getTypeName() = "NewWebBlockApiRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}
