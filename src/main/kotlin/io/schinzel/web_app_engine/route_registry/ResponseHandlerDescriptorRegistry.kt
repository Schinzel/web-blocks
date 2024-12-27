package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.IResponseHandler
import kotlin.reflect.KClass


/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object ResponseHandlerDescriptorRegistry {
    private val generators =
        mutableMapOf<KClass<out IResponseHandler>, IResponseHandlerDescriptor<out IResponseHandler>>()

    fun <T : IResponseHandler> register(
        processorType: KClass<T>,
        generator: IResponseHandlerDescriptor<T>
    ) {
        generators[processorType] = generator
    }


    @Suppress("UNCHECKED_CAST")
    fun getResponseHandlerDescriptor(clazz: KClass<out IResponseHandler>): IResponseHandlerDescriptor<IResponseHandler> {
        return generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IResponseHandlerDescriptor<IResponseHandler>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
    }

}