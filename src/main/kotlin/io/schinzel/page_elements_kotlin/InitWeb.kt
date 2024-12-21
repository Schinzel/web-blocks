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

                // Map query parameters to constructor arguments
                val params: Map<String, String> = route.arguments.associate { arg ->
                    // Try to get from POST body first
                    val postValue = ctx.formParam(arg.name)
                    // If not in POST, try query parameter
                    val value = postValue ?: ctx.queryParam(arg.name) ?: ""
                    "Value for ${arg.name} is $value".println()
                    arg.name to value
                }


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