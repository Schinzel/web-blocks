package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_mapping.Parameter
import io.schinzel.web_app_engine.route_registry.processors.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


// Register the default generators
fun initializeRouteRegistry() {
    RouteRegistry.register(IWebPage::class, WebPageRouteGenerator())
    RouteRegistry.register(IWebPageEndpoint::class, WebPageEndpointRouteGenerator())
    RouteRegistry.register(IEndpoint::class, EndpointRouteGenerator())
}


// The base interface for route generation strategy
interface IRouteGenerator<T : IRequestProcessor> {
    fun getPath(relativePath: String, clazz: KClass<out T>): String

    fun getTypeName(): String

    fun getReturnType(): ReturnTypeEnum
}

enum class ReturnTypeEnum { HTML, JSON }

/**
 * The purpose of this class is to store route generators.
 *
 * That is a class that can generate a route for a given class.
 */
object RouteRegistry {
    private val generators = mutableMapOf<KClass<out IRequestProcessor>, IRouteGenerator<out IRequestProcessor>>()
    fun <T : IRequestProcessor> register(
        processorType: KClass<T>,
        generator: IRouteGenerator<T>
    ) {
        generators[processorType] = generator
    }

    fun getPath(basePackage: String, clazz: KClass<out IRequestProcessor>): String {
        val generator = getGenerator(clazz)
        val relativePath = getRelativePath(basePackage, clazz)
        return generator.getPath(relativePath, clazz)
    }

    fun getTypeName(clazz: KClass<out IRequestProcessor>): String =
        getGenerator(clazz).getTypeName()

    @Suppress("UNCHECKED_CAST")
    fun getGenerator(clazz: KClass<out IRequestProcessor>): IRouteGenerator<IRequestProcessor> {
        return generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IRouteGenerator<IRequestProcessor>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
    }

}