package io.schinzel.web.response_handler_mapping

import io.schinzel.web.response_handlers.ResponseHandlerDescriptorRegistry
import io.schinzel.web.response_handlers.response_handlers.IResponseHandler
import io.schinzel.web.response_handlers.response_handlers.ReturnTypeEnum
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class to provide a mapping between
 * a response handler class and its path
 */
class ResponseHandlerMapping(
    val responseHandlerClass: KClass<out IResponseHandler>,
) {
    val parameters: List<Parameter> = getConstructorParameters(responseHandlerClass)
    val path: String

    // WebPage, API and so on
    val type: String
    val returnType: ReturnTypeEnum

    init {
        val responseHandlerDescriptor = ResponseHandlerDescriptorRegistry
            .getResponseHandlerDescriptor(responseHandlerClass)
        path = responseHandlerDescriptor.getPath(responseHandlerClass)
        type = responseHandlerDescriptor.getTypeName()
        returnType = responseHandlerDescriptor.getReturnType()
    }

    fun getPrimaryConstructor(): KFunction<IResponseHandler> {
        return responseHandlerClass.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${responseHandlerClass.simpleName}")
    }

    override fun toString(): String {
        return "RouteMapping(type='$type', path='$path', parameters=$parameters)"
    }
}
