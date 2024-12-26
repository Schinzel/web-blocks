package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.*
import kotlin.reflect.KClass


// Register the default generators
fun initializeRouteRegistry() {
    RouteRegistry.register(IWebPage::class, WebPagePath())
    RouteRegistry.register(IWebPageEndpoint::class, WebPageEndpointPath())
    RouteRegistry.register(IApiEndpoint::class, ApiEndpointPath())
}


// The base interface for route generation strategy
interface IEndpointPath<T : IEndpoint> {
    fun getPath(endpointPackage: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}

/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object RouteRegistry {
    private val generators = mutableMapOf<KClass<out IEndpoint>, IEndpointPath<out IEndpoint>>()
    fun <T : IEndpoint> register(
        processorType: KClass<T>,
        generator: IEndpointPath<T>
    ) {
        generators[processorType] = generator
    }


    @Suppress("UNCHECKED_CAST")
    fun getGenerator(clazz: KClass<out IEndpoint>): IEndpointPath<IEndpoint> {
        return generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IEndpointPath<IEndpoint>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
    }

}