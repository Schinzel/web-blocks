package io.schinzel.web_app_engine.route_mapping

import io.schinzel.web_app_engine.route_registry.ResponseHandlerDescriptorRegistry
import io.schinzel.web_app_engine.route_registry.response_handlers.IResponseHandler
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class is to map a route to a class.
 */
class RouteMapping(
    basePackage: String,
    val clazz: KClass<out IResponseHandler>,
) {
    val parameters: List<Parameter> = getConstructorParameters(clazz)
    val path: String = ResponseHandlerDescriptorRegistry
        .getResponseHandlerDescriptor(clazz)
        .getPath(basePackage, clazz)
    // WebPage, API and so on
    val type: String = ResponseHandlerDescriptorRegistry
        .getResponseHandlerDescriptor(clazz)
        .getTypeName()

    fun getPrimaryConstructor(): KFunction<IResponseHandler> {
        return clazz.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${clazz.simpleName}")
    }

    override fun toString(): String {
        return "RouteMapping(type='$type', path='$path', parameters=$parameters)"
    }

    companion object {
        private fun getConstructorParameters(clazz: KClass<out IResponseHandler>): List<Parameter> {
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
