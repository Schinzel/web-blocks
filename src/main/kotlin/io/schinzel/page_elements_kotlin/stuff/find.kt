package io.schinzel.page_elements_kotlin.stuff

import io.schinzel.basic_utils_kotlin.println
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

data class Route(
    val path: String,
    val clazz: KClass<*>,
    val parameters: List<Parameter>
) {
    override fun toString(): String {
        val type = when {
            IApi::class.java.isAssignableFrom(clazz.java) -> "API"
            IPage::class.java.isAssignableFrom(clazz.java) -> "Page"
            else -> "Unknown"
        }
        return "Type: $type, Path: $path, Class: ${clazz.simpleName}, Parameters: $parameters"
    }
}

data class Parameter(
    val name: String,
    val type: String
)

fun findRoutes(basePackage: String): List<Route> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IResponse::class.java)
        // Filter only classes in the base package and subpackages. This is needed because
        // Reflections will scan all classes in the classpath by default.
        .filter { clazz ->
            clazz.packageName.startsWith(basePackage) && !clazz.isInterface
        }
        .map { clazz ->
            // Get constructor parameters using Kotlin reflection
            val constructorParams = clazz.kotlin.primaryConstructor?.parameters
            val parameters = constructorParams?.map { param ->
                Parameter(
                    name = param.name ?: "",
                    type = param.type.toString()
                )
            }

            Route(
                path = clazz.packageName
                    .removePrefix(basePackage)
                    .removePrefix(".")
                    .replace(".", "/"),
                clazz = clazz.kotlin,
                parameters = parameters ?: emptyList()
            )
        }
        .toList()
}

// Usage
fun main() {
    "Find all classes that implement IResponse".println()
    findRoutes("io.schinzel.page_elements_kotlin.pages")
        .forEach { route ->
            route.toString().println()
        }
}
