package io.schinzel.page_elements_kotlin

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.IPage
import io.schinzel.page_elements_kotlin.stuff.Parameter
import io.schinzel.page_elements_kotlin.stuff.Route
import io.schinzel.page_elements_kotlin.stuff.findIResponseClasses
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor


fun main() {
    val javalin = Javalin.create { config ->
        config.staticFiles.add("/site", Location.CLASSPATH)
    }

    val pageRoutes = findIResponseClasses("io.schinzel.page_elements_kotlin.pages")
    val apiRoutes = findIResponseClasses("io.schinzel.page_elements_kotlin.apis")

    (pageRoutes + apiRoutes).forEach { route: Route ->
        route.toString().println()
        javalin.get(route.path) { ctx: Context ->

            // If no arguments, use default constructor
            val page = if (route.parameters.isEmpty()) {
                route.clazz.createInstance()
            } else {
                // If arguments, use constructor with arguments
                val arguments: Map<String, String> = getArguments(route.parameters, ctx)
                // Get constructor
                val constructor = route.clazz.primaryConstructor
                    ?: throw IllegalStateException("No primary constructor found for ${route.clazz.simpleName}")
                // Create instance with parameters
                constructor.callBy(
                    constructor.parameters.associateWith { param ->
                        arguments[param.name]
                    }
                )
            }

            if (page is IPage) {
                val response = page.getResponse()
                ctx.html(response)
            }
        }
    }
    javalin.start(5555)

    "*".repeat(30).println()
    "Project started".println()
    "*".repeat(30).println()
}


private fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, String> {
    return parameters.associate { arg ->
        // Try to get from POST body first
        val postValue = ctx.formParam(arg.name)
        // If not in POST, try query parameter
        val value = postValue ?: ctx.queryParam(arg.name) ?: ""
        "Value for ${arg.name} is $value".println()
        arg.name to value
    }
}