package io.schinzel.web_app_engine.response_handlers.response_handlers

import kotlin.reflect.KClass


/**
 * The purpose of this interface is to handle the response of a request
 */
interface IResponseHandler {
    fun getResponse(): Any

    fun getReturnType(): ReturnTypeEnum
}

enum class ReturnTypeEnum { HTML, JSON }


/**
 * The purpose of this interface it to provide information on a response handler
 * when we do not have an instance. For example when setting up the routes
 * we do not have instances, just classes.
 */
interface IResponseHandlerDescriptor<T : IResponseHandler> {
    fun getPath(endpointPackage: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}

