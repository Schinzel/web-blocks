package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.*
import kotlin.reflect.KClass


// Register the default generators
fun initializeRouteRegistry() {
    RouteRegistry.register(IWebPage::class, WebPageRouteGenerator())
    RouteRegistry.register(IWebPageEndpoint::class, WebPageEndpointRouteGenerator())
    RouteRegistry.register(IApiEndpoint::class, ApiEndpointRouteGenerator())
}


// The base interface for route generation strategy
interface IRouteGenerator<T : IEndpoint> {
    fun getPath(relativePath: String, clazz: KClass<out T>): String

    fun getTypeName(): String

}

/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object RouteRegistry {
    private val generators = mutableMapOf<KClass<out IEndpoint>, IRouteGenerator<out IEndpoint>>()
    fun <T : IEndpoint> register(
        processorType: KClass<T>,
        generator: IRouteGenerator<T>
    ) {
        generators[processorType] = generator
    }

    fun getPath(basePackage: String, clazz: KClass<out IEndpoint>): String {
        val generator = getGenerator(clazz)
        val relativePath = getRelativePath(basePackage, clazz)
        return generator.getPath(relativePath, clazz)
    }

    fun getTypeName(clazz: KClass<out IEndpoint>): String =
        getGenerator(clazz).getTypeName()

    @Suppress("UNCHECKED_CAST")
    private fun getGenerator(clazz: KClass<out IEndpoint>): IRouteGenerator<IEndpoint> {
        return generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IRouteGenerator<IEndpoint>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
    }

}