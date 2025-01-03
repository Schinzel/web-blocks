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
            .getRoutePath(this::class)
    }
}

/**
 * The purpose of this enum is to provide information
 * on the return type of response handlers
 */
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
    fun getRoutePath(clazz: KClass<out T>): String

    /**
     * @return The return type of IResponseHandler.getResponse().
     * For example HTML or JSON
     */
    fun getReturnType(): ReturnTypeEnum

    /**
     * @return The type name of the response handler.
     * For example "WebPage" or "API"
     * For user notification and logging purposes
     */
    fun getTypeName(): String
}
