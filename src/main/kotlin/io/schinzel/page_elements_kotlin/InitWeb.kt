package io.schinzel.page_elements_kotlin

import io.javalin.Javalin
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.IPage
import io.schinzel.page_elements_kotlin.stuff.find_pages.find_ipages.PageRoute
import io.schinzel.page_elements_kotlin.stuff.find_pages.find_ipages.findIPageClasses
import kotlin.reflect.full.createInstance


fun main() {
    val javalin = Javalin.create { config ->
        config.staticFiles.add("/site", Location.CLASSPATH)
    }
        .get("/api/ping") { ctx -> ctx.result("pong") }

    findIPageClasses("io.schinzel.page_elements_kotlin.pages")
        .forEach { route: PageRoute ->
            println("Created route: $route")
            javalin.get(route.path) { ctx ->
                val page = route.pageClass.createInstance()
                if (page is IPage) {
                    val htmlContent = page.getHtml()
                    ctx.html(htmlContent)
                }
            }
        }
    javalin.start(5555)
    "*".repeat(30).println()
    "Project started".println()
    "*".repeat(30).println()
}