package io.schinzel.page_elements.set_up_routes

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.endpoint.Parameter
import io.schinzel.page_elements.endpoint.Endpoint
import io.schinzel.page_elements.web_response.IWebResponse
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.full.primaryConstructor

fun findRoutes(basePackage: String): List<Endpoint> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IWebResponse::class.java)
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


            Endpoint(
                basePackage,
                clazz = clazz.kotlin,
                parameters = parameters ?: emptyList()
            )
        }
        .toList()
}

// Usage
fun main() {
    "Find all page routes".println()
    findRoutes("io.schinzel.sample.pages")
        .forEach { route ->
            route.toString().println()
        }
    "Find all api routes".println()
    findRoutes("io.schinzel.sample.apis")
        .forEach { route ->
            route.toString().println()
        }

}
