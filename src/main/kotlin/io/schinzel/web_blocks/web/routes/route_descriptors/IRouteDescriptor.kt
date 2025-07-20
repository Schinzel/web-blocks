package io.schinzel.web_blocks.web.routes.route_descriptors

import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import kotlin.reflect.KClass

/**
 * The purpose of this interface it to provide information on a route
 * when we do not have an instance. For example when setting up the routes
 * we do not have instances, just classes.
 */
interface IRouteDescriptor<T : IRoute> {
    val pathPrefix: String
    val suffixesToRemove: List<String>
    val returnType: ReturnTypeEnum
    val annotation: KClass<out Annotation>
    /**
     * @param routeClass The class of the route
     * @return The path of the route
     */
    fun getRoutePath(routeClass: KClass<out T>): String

    fun getRoutePathFromRelativePath(
        relativePathRouteClass: String,
        classSimpleName: String,
    ): String

    /**
     * @return The type name of the route.
     * For example "WebPage" or "API"
     * For user notification and logging purposes
     */
    fun getTypeName(): String
}
