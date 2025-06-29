package io.schinzel.web_blocks.web.route_mapping

import io.schinzel.web_blocks.web.routes.RouteDescriptorRegistry
import io.schinzel.web_blocks.web.routes.IRoute
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum
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
    val path: String

    // WebPage, API and so on
    val type: String
    val returnType: ReturnTypeEnum

    init {
        val routeDescriptor = RouteDescriptorRegistry
            .getRouteDescriptor(routeClass)
        path = routeDescriptor.getRoutePath(routeClass)
        type = routeDescriptor.getTypeName()
        returnType = routeDescriptor.getReturnType()
    }

    fun getPrimaryConstructor(): KFunction<IRoute> {
        return routeClass.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${routeClass.simpleName}")
    }

    override fun toString(): String {
        return "RouteMapping(type='$type', path='$path', parameters=$parameters)"
    }
}
