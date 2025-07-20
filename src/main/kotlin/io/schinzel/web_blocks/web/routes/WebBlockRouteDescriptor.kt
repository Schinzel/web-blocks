package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this class is to generate route paths for classes annotated with @WebBlock.
 *
 * WebBlock routes generate paths with /web-block/ prefix and follow directory structure
 * from the source file location. Class names are converted from PascalCase to kebab-case
 * with "Block" suffix removed if present.
 *
 * Path generation examples:
 * - /pages/profile/blocks/avatar_block/AvatarBlock.kt → /web-block/profile/blocks/avatar-block/avatar-block
 * - /pages/dashboard/widgets/weather/WeatherWidget.kt → /web-block/dashboard/widgets/weather/weather-widget
 *
 * Written by Claude Sonnet 4
 */
class WebBlockRouteDescriptor(
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

        // Ensure class has @WebBlock annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.WEB_BLOCK) {
            throw IllegalArgumentException(
                "Class ${clazz.simpleName} is not annotated with @WebBlock",
            )
        }

        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = RouteUtil.removeSuffixesAndToKebabCase(clazz, listOf())
        val returnPath = "web-block/$pagePathWithoutPages/$classNameKebabCase"

        // Validate against system paths (excluding web-block since it's our prefix)
        val pathAfterPrefix = returnPath.removePrefix("web-block/")
        systemPaths.filter { it != "web-block" }.forEach { systemPath ->
            if (pathAfterPrefix.startsWith(systemPath)) {
                throw IllegalArgumentException(
                    "WebBlock path cannot start with '$systemPath' after web-block/ prefix. Path: '$returnPath'",
                )
            }
        }

        return returnPath
    }

    override fun getTypeName() = "WebBlockRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}
