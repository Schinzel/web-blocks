package io.schinzel.web.response_handlers

import kotlin.reflect.KClass


/**
 * The purpose of this interface is to handle the response of a request
 */
interface IResponseHandler {
    fun getResponse(): Any

    fun getPath(): String {
        return ResponseHandlerDescriptorRegistry
            .getResponseHandlerDescriptor(this::class)
            .getPath(this::class)
    }

}

enum class ReturnTypeEnum { HTML, JSON }


/**
 * The purpose of this interface it to provide information on a response handler
 * when we do not have an instance. For example when setting up the routes
 * we do not have instances, just classes.
 */
interface IResponseHandlerDescriptor<T : IResponseHandler> {
    /**
     * @param clazz The class of the response handler
     * @return The path or route of the response handler
     */
    fun getPath(clazz: KClass<out T>): String

    // WebPage, API and so on. For user notification and logging purposes
    fun getTypeName(): String

    fun getReturnType(): ReturnTypeEnum
}

