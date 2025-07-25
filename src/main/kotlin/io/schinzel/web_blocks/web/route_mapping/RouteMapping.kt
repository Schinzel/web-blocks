package io.schinzel.web_blocks.web.route_mapping

import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class to provide a mapping between
 * a route class and its path
 */
class RouteMapping(
    val routeClass: KClass<out IRoute>,
) {
    val parameters: List<Parameter> = getConstructorParameters(routeClass)
    val routePath: String

    // WebPage, API and so on
    val type: String
    val returnType: ReturnTypeEnum

    init {
        val routeDescriptor = RouteDescriptorRegistry.getRouteDescriptor(routeClass)
        routePath = routeDescriptor.getRoutePath(routeClass)
        type = routeDescriptor.getTypeName()
        returnType = routeDescriptor.returnType
    }

    fun getPrimaryConstructor(): KFunction<IRoute> =
        routeClass.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${routeClass.simpleName}")

    override fun toString(): String = "RouteMapping(type='$type', path='$routePath', parameters=$parameters)"
}
