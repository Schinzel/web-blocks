package io.schinzel.page_elements.route_mapping

import io.schinzel.page_elements.route_mapping.path.EndpointRoute
import io.schinzel.page_elements.route_mapping.path.IRoute
import io.schinzel.page_elements.route_mapping.path.PageRoute
import io.schinzel.page_elements.web_response.IEndpoint
import io.schinzel.page_elements.web_response.IRequestProcessor
import io.schinzel.page_elements.web_response.IWebPage
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * The purpose of this class is to map a route to a class.
 */
class RouteMapping(
    basePackage: String,
    val clazz: KClass<out IRequestProcessor>,
) {
    private val isPage = IWebPage::class.java.isAssignableFrom(clazz.java)
    private val isEndpoint = IEndpoint::class.java.isAssignableFrom(clazz.java)
    private val route: IRoute
    val parameters: List<Parameter>


    init {
        // Convert the package name to a path relative to the base package
        val relativePath = getRelativePath(basePackage, clazz)
        route = when {
            isPage -> PageRoute(relativePath)
            isEndpoint -> EndpointRoute(relativePath, clazz)
            else -> throw Exception("Class ${clazz.simpleName} does not implement IPage or IEndpoint")
        }
        parameters = getConstructorParameter(clazz)
    }

    fun getRoutePath(): String = route.path

    fun getType(): String = when {
        isPage -> "Page"
        isEndpoint -> "Endpoint"
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

