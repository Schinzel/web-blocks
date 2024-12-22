package io.schinzel.page_elements.route

import io.schinzel.page_elements.IApi
import io.schinzel.page_elements.IPage
import io.schinzel.page_elements.IWebResponse
import io.schinzel.page_elements.route.path.ApiPath
import io.schinzel.page_elements.route.path.IPath
import io.schinzel.page_elements.route.path.PagePath
import kotlin.reflect.KClass

data class Route(
    private val basePackage: String,
    val clazz: KClass<out IWebResponse>,
    val parameters: List<Parameter>
) {
    private val isPage = IPage::class.java.isAssignableFrom(clazz.java)
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


    override fun toString(): String {
        val type = when {
            isPage -> "Page"
            isApi -> "Api"
            else -> "Unknown"
        }
        return "Type: $type, Path: ${getPath()}, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}

