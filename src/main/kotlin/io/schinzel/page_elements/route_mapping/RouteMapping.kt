package io.schinzel.page_elements.route_mapping

import io.schinzel.page_elements.route_mapping.path.EndpointRoute
import io.schinzel.page_elements.route_mapping.path.IRoute
import io.schinzel.page_elements.route_mapping.path.PageRoute
import io.schinzel.page_elements.web_response.IEndpoint
import io.schinzel.page_elements.web_response.IWebPage
import io.schinzel.page_elements.web_response.IRequestProcessor
import kotlin.reflect.KClass

class RouteMapping(
    basePackage: String,
    val clazz: KClass<out IRequestProcessor>,
    val parameters: List<Parameter>
) {
    private val isPage = IWebPage::class.java.isAssignableFrom(clazz.java)
    private val isEndpoint = IEndpoint::class.java.isAssignableFrom(clazz.java)
    private val route: IRoute


    init {
        // Convert the package name to a path relative to the base package
        val relativePath = clazz.java.packageName
            .removePrefix(basePackage)
            .removePrefix(".")
            .replace(".", "/")
            // Convert from snake case to kebab case
            .replace("_", "-")
        route = when {
            isPage -> PageRoute(relativePath)
            isEndpoint -> EndpointRoute(relativePath, clazz)
            else -> throw Exception("Class ${clazz.simpleName} does not implement IPage or IEndpoint")
        }
    }

    fun getRoute(): String = route.path

    fun getType(): String = when {
        isPage -> "Page"
        isEndpoint -> "Endpoint"
        else -> "Unknown"
    }

    override fun toString(): String {
        return "Type: ${this.getType()}, Route: ${getRoute()}, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}

