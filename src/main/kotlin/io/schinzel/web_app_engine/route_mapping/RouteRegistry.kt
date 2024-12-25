package io.schinzel.web_app_engine.route_mapping

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.web_app_engine.IEndpoint
import io.schinzel.web_app_engine.IRequestProcessor
import io.schinzel.web_app_engine.IWebPage
import io.schinzel.web_app_engine.IWebPageEndpoint
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

    fun getConstructorParameters(): List<Parameter> {
        // Get constructor parameters using Kotlin reflection
        val constructorParams = this::class.primaryConstructor?.parameters
        return (constructorParams
            ?.map { param ->
                Parameter(
                    name = param.name ?: "",
                    type = param.type.toString()
                )
            }
            ?: emptyList())
    }

    fun getTypeName(): String
}

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

    @Suppress("UNCHECKED_CAST")
    fun getPath(basePackage: String, clazz: KClass<out IRequestProcessor>): String {
        val generator = getGenerator(clazz)

        val relativePath = getRelativePath(basePackage, clazz)

        return generator.getPath(relativePath, clazz)
    }

    fun getTypeName(clazz: KClass<out IRequestProcessor>): String =
        this.getGenerator(clazz).getTypeName()

    private fun getGenerator(clazz: KClass<out IRequestProcessor>): IRouteGenerator<IRequestProcessor> {
        return generators.entries
            .find { (interfaceType, _) ->
                interfaceType.java.isAssignableFrom(clazz.java)
            }
            ?.value as? IRouteGenerator<IRequestProcessor>
            ?: throw IllegalArgumentException("No route generator registered for ${clazz.simpleName}")
    }

}


class WebPageRouteGenerator : IRouteGenerator<IWebPage> {
    override fun getPath(relativePath: String, clazz: KClass<out IWebPage>): String {
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName(): String = "WebPage"
}

class WebPageEndpointRouteGenerator : IRouteGenerator<IWebPageEndpoint> {
    override fun getPath(relativePath: String, clazz: KClass<out IWebPageEndpoint>): String {
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName(): String = "WebPageEndpoint"

}

class EndpointRouteGenerator : IRouteGenerator<IEndpoint> {
    override fun getPath(relativePath: String, clazz: KClass<out IEndpoint>): String {
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "$relativePath/$classNameKebabCase"
    }

    override fun getTypeName(): String = "EndPoing"
}


private fun getRelativePath(basePackage: String, clazz: KClass<out IRequestProcessor>): String =
    clazz.java
        .packageName
        .removePrefix(basePackage)
        .removePrefix(".")
        .replace(".", "/")
        .replace("_", "-")


private fun getClassNameAsKebabCase(clazz: KClass<*>): String {
    return clazz.simpleName!!
        .removeSuffix("WebPageEndpoint")
        .removeSuffix("Endpoint")
        .removeSuffix("Api")
        .let { StandardTextCases.PASCAL_CASE.convertTo(StandardTextCases.KEBAB_CASE, it) }
}