package io.schinzel.page_elements_kotlin.stuff.annotations

import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

data class PageRoute(
    val path: String,
    val pageClass: KClass<*>
)

fun findPageClasses(basePackage: String): List<PageRoute> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            .setScanners(Scanners.TypesAnnotated)
    )

    return reflections
        .getTypesAnnotatedWith(Page::class.java)
        .map { clazz ->
            PageRoute(
                path = clazz.packageName
                    .removePrefix(basePackage)
                    .removePrefix(".")
                    .replace(".", "/"),
                pageClass = clazz.kotlin
            )
        }
        .toList()
}

// Usage
fun main() {
    val pages = findPageClasses("io.schinzel.page_elements_kotlin")
    pages.forEach { route ->
        println("Route: ${route.path}")
        //println("Class: ${route.pageClass}")
        // You can create an instance like this:
        // val instance = route.pageClass.createInstance()
    }
}