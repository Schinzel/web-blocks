package io.schinzel.page_elements.endpoint

import io.schinzel.page_elements.endpoint.path.ApiPath
import io.schinzel.page_elements.endpoint.path.IPath
import io.schinzel.page_elements.endpoint.path.PagePath
import io.schinzel.page_elements.web_response.IApi
import io.schinzel.page_elements.web_response.IWebPage
import io.schinzel.page_elements.web_response.IWebResponse
import kotlin.reflect.KClass

class Endpoint(
    basePackage: String,
    val clazz: KClass<out IWebResponse>,
    val parameters: List<Parameter>
) {
    private val isPage = IWebPage::class.java.isAssignableFrom(clazz.java)
    private val isApi = IApi::class.java.isAssignableFrom(clazz.java)
    private val path: IPath


    init {
        // Convert the package name to a path relative to the base package
        val relativePath = clazz.java.packageName
            .removePrefix(basePackage)
            .removePrefix(".")
            .replace(".", "/")
            // Convert from snake case to kebab case
            .replace("_", "-")
        path = when {
            isPage -> PagePath(relativePath)
            isApi -> ApiPath(relativePath, clazz)
            else -> throw Exception("Class ${clazz.simpleName} does not implement IPage or IApi")
        }
    }

    fun getPath(): String = path.path

    fun getType(): String = when {
        isPage -> "Page"
        isApi -> "Api"
        else -> "Unknown"
    }

    override fun toString(): String {
        return "Type: ${this.getType()}, Path: ${getPath()}, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}

