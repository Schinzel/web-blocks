package io.schinzel.page_elements_kotlin

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.annotations.findPageClasses


fun main() {
    "*".repeat(30).println()
    "Project started.".println()
    "*".repeat(30).println()

    val pages = findPageClasses("io.schinzel.page_elements_kotlin")
    pages.forEach { route ->
        println("Route: ${route.path}")
    }
}