package io.schinzel.web_blocks.web.routes.route_descriptors

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.annotations.PageBlock
import kotlin.reflect.KClass

/**
 */
class RouteDescriptorPageBlock(
    private val endpointPackage: String,
) : IRouteDescriptor<IRoute> {
    override val pathPrefix: String = "page-block"
    override val suffixesToRemove: List<String> = listOf("PageBlock", "Pb", "PB", "Block")
    override val returnType = ReturnTypeEnum.HTML
    override val annotation: KClass<out Annotation> = PageBlock::class

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
            relativePathRouteClass = relativePathRouteClass,
            classSimpleName = routeClass.simpleName!!,
        )
    }

    override fun getRoutePathFromRelativePath(
        relativePathRouteClass: String,
        classSimpleName: String,
    ): String {
        // Remove suffixes
        val clazzSimpleNameNoSuffixes =
            RouteUtil
                .removeSuffixes(classSimpleName, suffixesToRemove)
        // Convert to kebab case
        val classNameNoSuffixesKebabCase =
            RouteUtil
                .toKebabCase(clazzSimpleNameNoSuffixes)
        val relativePathRouteClassKebabCase =
            StandardTextCases.SNAKE_CASE
                .convertTo(StandardTextCases.KEBAB_CASE, relativePathRouteClass)
        // Remove pages/ from path
        val relativePathRouteClassWithoutPages =
            relativePathRouteClassKebabCase
                .removePrefix("pages/")
        // Compile route path
        return "/page-block/$relativePathRouteClassWithoutPages/$classNameNoSuffixesKebabCase"
    }

    override fun getTypeName() = "PageBlockRoute"
}
