package io.schinzel.web_blocks.web.routes

import kotlin.reflect.KClass

/**
 * The purpose of this interface it to provide information on a route
 * when we do not have an instance. For example when setting up the routes
 * we do not have instances, just classes.
 */
interface IRouteDescriptor<T : IRoute> {
    /**
     * @param clazz The class of the route
     * @return The path of the route
     */
    fun getRoutePath(clazz: KClass<out T>): String

    /**
     * @return The return type of IRoute.getResponse().
     * For example HTML or JSON
     */
    fun getReturnType(): ReturnTypeEnum

    /**
     * @return The type name of the route.
     * For example "WebPage" or "API"
     * For user notification and logging purposes
     */
    fun getTypeName(): String
}
