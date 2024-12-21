package io.schinzel.page_elements_kotlin

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.find_pages.annotations.findPageAnnotations
import io.schinzel.page_elements_kotlin.stuff.find_pages.find_ipages.findIPageClasses


fun main() {
    "*".repeat(30).println()
    "Project started.".println()
    "*".repeat(30).println()

    "Find all classes that have the annotation @Page".println()
    findPageAnnotations("io.schinzel.page_elements_kotlin").forEach { route ->
        println("Route: ${route.path}")
    }


    "*".repeat(30).println()
    "Find all classes that implement IPage".println()
    findIPageClasses("io.schinzel.page_elements_kotlin").forEach { route ->
        println("Route: ${route.path}")
    }

}