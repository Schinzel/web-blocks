package io.schinzel.web_app_engine.route_mapping

import io.schinzel.web_app_engine.IEndpoint
import io.schinzel.web_app_engine.IRequestProcessor
import io.schinzel.web_app_engine.IWebPage
import io.schinzel.web_app_engine.IWebPageEndpoint
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class RouteMapping2(
    basePackage: String,
    val clazz: KClass<out IRequestProcessor>,
) {
    val parameters: List<Parameter> = getConstructorParameter(clazz)
    val path: String = RouteRegistry.getPath(basePackage, clazz)
    val type: String = RouteRegistry.getTypeName(clazz)

    fun getPrimaryConstructor(): KFunction<IRequestProcessor> {
        return clazz.primaryConstructor
            ?: throw IllegalStateException("No primary constructor found for ${clazz.simpleName}")
    }

    companion object {
        private fun getConstructorParameter(clazz: KClass<out IRequestProcessor>): List<Parameter> {
            // Get constructor parameters using Kotlin reflection
            val constructorParams = clazz.primaryConstructor?.parameters
            return (constructorParams
                ?.map { param ->
                    Parameter(
                        name = param.name ?: "",
                        type = param.type.toString()
                    )
                }
                ?: emptyList())
        }
    }
}


/**
 * The purpose of this class is to map a route to a class.
 */
class RouteMapping(
    basePackage: String,
    val clazz: KClass<out IRequestProcessor>,
) {
    private val isWebPage = IWebPage::class.java.isAssignableFrom(clazz.java)
    private val isEndpoint = IEndpoint::class.java.isAssignableFrom(clazz.java)
    private val isWebPageEndpoint = IWebPageEndpoint::class.java.isAssignableFrom(clazz.java)
    private val route: IRoute
    val parameters: List<Parameter>


    init {
        // Convert the package name to a path relative to the base package
        val relativePath = getRelativePath(basePackage, clazz)
        route = when {
            isWebPage -> PageRoute(relativePath)
            isEndpoint -> EndpointRoute(relativePath, clazz)
            isWebPageEndpoint -> WebPageEndPoint(relativePath, clazz)
            else -> throw Exception("Class ${clazz.simpleName} does not implement IPage or IEndpoint")
        }
        parameters = getConstructorParameter(clazz)
    }

    fun getRoutePath(): String = route.path

    fun getType(): String = when {
        isWebPage -> "Page"
        isEndpoint -> "Endpoint"
        isWebPageEndpoint -> "WebPageEndpoint"
        else -> "Unknown"
    }

    override fun toString(): String = "Type: ${this.getType()}, " +
            "Route: ${getRoutePath()}, " +
            "Class: ${clazz.simpleName}, " +
            "Parameters: $parameters"

    companion object {

        fun getRelativePath(basePackage: String, clazz: KClass<out IRequestProcessor>): String = clazz.java
            .packageName
            .removePrefix(basePackage)
            .removePrefix(".")
            .replace(".", "/")
            .replace("_", "-")


        fun getConstructorParameter(clazz: KClass<out IRequestProcessor>): List<Parameter> {
            // Get constructor parameters using Kotlin reflection
            val constructorParams = clazz.primaryConstructor?.parameters
            return (constructorParams
                ?.map { param ->
                    Parameter(
                        name = param.name ?: "",
                        type = param.type.toString()
                    )
                }
                ?: emptyList())
        }
    }
}

