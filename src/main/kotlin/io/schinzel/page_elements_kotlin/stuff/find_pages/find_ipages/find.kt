package io.schinzel.page_elements_kotlin.stuff.find_pages.find_ipages

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.IPage
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

data class PageRoute(
    val path: String,
    val pageClass: KClass<*>,
    val parameters: List<Parameter>
) {
    override fun toString(): String {
        return "Path: $path, PageClass: ${pageClass.simpleName}, Parameters: $parameters"
    }
}

data class Parameter(
    val name: String,
    val type: String
)

fun findIPageClasses(basePackage: String): List<PageRoute> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            // Filter only classes in the base package and subpackages. This is needed because
            // Reflections will scan all classes in the classpath by default.
            .filterInputsBy { input ->
                input != null && input == basePackage || input.startsWith("$basePackage.")
            }
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IPage::class.java)
        .map { clazz ->
            // Get constructor parameters using Kotlin reflection
            val constructorParams = clazz.kotlin.primaryConstructor?.parameters
            val parameters = constructorParams?.map { param ->
                Parameter(
                    name = param.name ?: "",
                    type = param.type.toString()
                )
            }

            PageRoute(
                path = clazz.packageName
                    .removePrefix(basePackage)
                    .removePrefix(".")
                    .replace(".", "/"),
                pageClass = clazz.kotlin,
                parameters = parameters ?: emptyList()
            )
        }
        .toList()
}

// Usage
fun main() {
    "Find all classes that implement IPage".println()
    val pages = findIPageClasses("io.schinzel.page_elements_kotlin.pages")
    pages.forEach { route ->
        println("Route: $route")
    }
}
