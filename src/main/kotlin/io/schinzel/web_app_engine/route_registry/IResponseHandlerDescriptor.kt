package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.IResponseHandler
import kotlin.reflect.KClass

/**
 * This is needed as some properties we need to derive using the
 * class, as we do not have an instance of the endpoint when setting
 * up the routes.
 */
interface IResponseHandlerDescriptor<T : IResponseHandler> {
    fun getPath(endpointPackage: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}