package io.schinzel.web_blocks.web.routes.route_descriptors

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.annotations.RouteAnnotationUtil
import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum
import kotlin.reflect.KClass

/**
 */
class RouteDescriptorPage(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    override val pathPrefix: String = ""
    override val suffixesToRemove: List<String> = emptyList()

    // Static and the start of the route-paths
    private val systemPaths = listOf("static", "api", "page-block", "page-block-api")

    override fun getRoutePath(routeClass: KClass<out IRoute>): String {
        // Ensure class implements IWebBlockRoute
        if (!IWebBlockRoute::class.java.isAssignableFrom(routeClass.java)) {
            throw IllegalArgumentException(
                "Class ${routeClass.simpleName} must implement IWebBlockRoute",
            )
        }

        @Suppress("UNCHECKED_CAST")
        val webBlockRouteClass = routeClass as KClass<out IWebBlockRoute<*>>

        // Validate annotation
        RouteAnnotationUtil.validateRouteAnnotation(webBlockRouteClass)

        // Ensure class has @WebBlockPage annotation
        if (RouteAnnotationUtil.detectRouteType(webBlockRouteClass) != RouteTypeEnum.PAGE) {
            throw IllegalArgumentException(
                "Class ${routeClass.simpleName} is not annotated with @WebBlockPage",
            )
        }

        val relativePathRouteClass = RouteUtil.getRelativePath(endpointPackage, routeClass)
        return getRoutePathFromRelativePath(relativePathRouteClass, "")
    }


    override fun getRoutePathFromRelativePath(
        relativePathRouteClass: String,
        classSimpleName: String,
    ): String {
        val relativePathRouteClassKebabCase = StandardTextCases.SNAKE_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, relativePathRouteClass)
        // Remove pages/ from start of path
        val pagePathWithoutPages = relativePathRouteClassKebabCase.removePrefix("pages/")
        val returnPath = if (pagePathWithoutPages == "landing") "/" else "/$pagePathWithoutPages"
        // Go through the system paths
        systemPaths.forEach { systemPath ->
            // If the return path start with
            if (returnPath.removePrefix("/").startsWith(systemPath)) {
                throw IllegalArgumentException(
                    "Page path cannot start with '$systemPath'. Page path: '$returnPath'",
                )
            }
        }
        return returnPath
    }

    override fun getTypeName() = "PageRoute"

    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}
