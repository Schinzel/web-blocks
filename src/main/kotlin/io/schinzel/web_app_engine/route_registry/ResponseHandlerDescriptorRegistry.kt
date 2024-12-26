package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.*
import kotlin.reflect.KClass


// Register the default descriptors
fun initializeResponseHandlerDescriptorRegistry() {
    ResponseHandlerDescriptorRegistry.register(IPageResponseHandler::class, PageResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry.register(IPageEndpointResponseHandler::class, PageEndpointResponseHandlerDescriptor())
    ResponseHandlerDescriptorRegistry.register(IEndpointResponseHandler::class, EndpointResponseHandlerDescriptor())
}


/**
 * This is needed as some properties we need to derive using the
 * class, as we do not have an instance of the endpoint when setting
 * up the routes.
 */
interface IResponseHandlerDescriptor<T : IResponseHandler> {
    fun getPath(endpointPackage: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}

/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object ResponseHandlerDescriptorRegistry {
    private val generators = mutableMapOf<KClass<out IResponseHandler>, IResponseHandlerDescriptor<out IResponseHandler>>()
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