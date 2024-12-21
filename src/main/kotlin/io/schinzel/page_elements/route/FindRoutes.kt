package io.schinzel.page_elements.route

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.IResponse
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.full.primaryConstructor

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
