package io.schinzel.page_elements_kotlin.stuff.find_pages.find_ipages

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.IPage
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

data class PageRoute(
    val path: String,
    val pageClass: KClass<*>
)

fun findIPageClasses(basePackage: String): List<PageRoute> {
    val reflections = Reflections(
        ConfigurationBuilder()
            .forPackage(basePackage)
            .setScanners(Scanners.SubTypes)
    )

    return reflections
        .getSubTypesOf(IPage::class.java)
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
    "Find all classes that implement IPage".println()
    val pages = findIPageClasses("io.schinzel.page_elements_kotlin")
    pages.forEach { route ->
        println("Route: ${route.path}")
        //println("Class: ${route.pageClass}")
    }
}
