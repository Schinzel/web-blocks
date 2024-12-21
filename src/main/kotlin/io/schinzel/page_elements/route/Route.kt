package io.schinzel.page_elements.route

import io.schinzel.page_elements.IApi
import io.schinzel.page_elements.IPage
import kotlin.reflect.KClass

data class Route(
    private val path: String,
    val clazz: KClass<*>,
    val parameters: List<Parameter>
) {

    fun getPath(): String {
        if (path == "landing"){
            return "/"
        }
        return path
    }

    override fun toString(): String {
        val type = when {
            IApi::class.java.isAssignableFrom(clazz.java) -> "API"
            IPage::class.java.isAssignableFrom(clazz.java) -> "Page"
            else -> "Unknown"
        }
        return "Type: $type, Path: ${this.getPath()}, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}