package io.schinzel.web_app_engine.route_mapping

import io.schinzel.web_app_engine.route_registry.RouteRegistry
import io.schinzel.web_app_engine.route_registry.processors.IRequestProcessor
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class is to map a route to a class.
 */
class RouteMapping(
    basePackage: String,
    val clazz: KClass<out IRequestProcessor>,
) {
    val parameters: List<Parameter> = getConstructorParameter(clazz)
    val path: String = RouteRegistry.getPath(basePackage, clazz)
    val type: String = RouteRegistry.getTypeName(clazz)

    fun getPrimaryConstructor(): KFunction<IRequestProcessor> {
        return clazz.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${clazz.simpleName}")
    }

    override fun toString(): String {
        return "RouteMapping(type='$type', path='$path', parameters=$parameters)"
    }

    companion object {
        private fun getConstructorParameter(clazz: KClass<out IRequestProcessor>): List<Parameter> {
            // Get constructor parameters using Kotlin reflection
            val constructorParams = clazz.primaryConstructor?.parameters
            return (constructorParams
                ?.map { param ->
                    Parameter(
                        name = param.name ?: "",
                        type = param.type
                    )
                }
                ?: emptyList())
        }
    }
}
