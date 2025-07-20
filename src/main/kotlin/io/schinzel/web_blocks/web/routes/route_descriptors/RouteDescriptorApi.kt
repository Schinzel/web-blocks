package io.schinzel.web_blocks.web.routes.route_descriptors

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.annotations.Api
import kotlin.reflect.KClass

/**
 */
class RouteDescriptorApi(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    override val pathPrefix: String = "api"
    override val suffixesToRemove: List<String> = listOf("ApiRoute", "Api", "API", "Route")
    override val returnType = ReturnTypeEnum.JSON
    override val annotation: KClass<out Annotation> = Api::class

    override fun getRoutePath(routeClass: KClass<out IRoute>): String {
        // Ensure class implements IWebBlockRoute
        if (!IWebBlockRoute::class.java.isAssignableFrom(routeClass.java)) {
            throw IllegalArgumentException(
                "Class ${routeClass.simpleName} must implement IWebBlockRoute",
            )
        }

        // Ensure class has correct annotation
        validateRouteAnnotation(routeClass)

        val relativePathRouteClass = RouteUtil.getRelativePath(endpointPackage, routeClass)
        return getRoutePathFromRelativePath(
            relativePathRouteClass,
            routeClass.simpleName!!,
        )
    }

    override fun getRoutePathFromRelativePath(
        relativePathRouteClass: String,
        classSimpleName: String,
    ): String {
        val relativePathRouteClassKebabCase =
            StandardTextCases.SNAKE_CASE
                .convertTo(StandardTextCases.KEBAB_CASE, relativePathRouteClass)
        // Remove suffixes
        val clazzSimpleNameNoSuffixes =
            RouteUtil
                .removeSuffixes(classSimpleName, suffixesToRemove)
        // Convert to kebab case
        val classNameNoSuffixesKebabCase =
            RouteUtil
                .toKebabCase(clazzSimpleNameNoSuffixes)
        // Compile route path
        return "/$relativePathRouteClassKebabCase/$classNameNoSuffixesKebabCase"
    }

    override fun getTypeName() = "ApiRoute"
}
