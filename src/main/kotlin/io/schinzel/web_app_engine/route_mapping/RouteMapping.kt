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
    endpointPackage: String,
    val responseHandlerClass: KClass<out IResponseHandler>,
) {
    val parameters: List<Parameter> = getConstructorParameters(responseHandlerClass)
    val path: String

    // WebPage, API and so on
    val type: String

    init {
        val responseHandlerDescriptor = ResponseHandlerDescriptorRegistry
            .getResponseHandlerDescriptor(responseHandlerClass)
        path = responseHandlerDescriptor.getPath(endpointPackage, responseHandlerClass)
        type = responseHandlerDescriptor.getTypeName()
    }

    fun getPrimaryConstructor(): KFunction<IResponseHandler> {
        return responseHandlerClass.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${responseHandlerClass.simpleName}")
    }

    override fun toString(): String {
        return "RouteMapping(type='$type', path='$path', parameters=$parameters)"
    }
}
