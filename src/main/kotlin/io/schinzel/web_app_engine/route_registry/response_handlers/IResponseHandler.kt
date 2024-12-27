package io.schinzel.web_app_engine.route_registry.response_handlers

import kotlin.reflect.KClass


interface IResponseHandler {
    fun getResponse(): Any

    fun getReturnType(): ReturnTypeEnum
}

enum class ReturnTypeEnum { HTML, JSON }


/**
 * This is needed as some properties we need to derive using the
 * class, as we do not have an instance of the endpoint when setting
 * up the routes.
 */
interface IResponseHandlerDescriptor<T : IResponseHandler> {
    fun getPath(endpointPackage: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}

