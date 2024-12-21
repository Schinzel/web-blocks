package io.schinzel.page_elements.route

import io.schinzel.page_elements.IApi
import io.schinzel.page_elements.IPage
import io.schinzel.sample.pages.settings.name.Page
import kotlin.reflect.KClass

data class Route(
    private val basePackage: String,
    val clazz: KClass<*>,
    val parameters: List<Parameter>
) {
    private val isApi = IApi::class.java.isAssignableFrom(clazz.java)
    private val isPage = IPage::class.java.isAssignableFrom(clazz.java)
    private val path: IPath


    init {
        val relativePath = clazz.java.packageName
            .removePrefix(basePackage)
            .removePrefix(".")
            .replace(".", "/")
        path = when {
            isPage -> PagePath(relativePath)
            isApi -> ApiPath(relativePath)
            else -> throw Exception("Class ${clazz.simpleName} does not implement IPage or IApi")
        }
    }

    fun getPath(): String = path.path


    override fun toString(): String {
        val type = when {
            isApi -> "Api"
            isPage -> "Page"
            else -> "Unknown"
        }
        return "Type: $type, Path: ${getPath()}, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}

interface IPath {
    val path: String
}

class PagePath(relativePath: String) : IPath {
    override val path: String

    init {
        path = if (relativePath == "landing") {
            "/"
        } else {
            relativePath
        }
    }
}

class ApiPath(relativePath: String) : IPath {
    override val path: String = relativePath
}