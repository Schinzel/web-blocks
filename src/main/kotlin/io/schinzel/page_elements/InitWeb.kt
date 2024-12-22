package io.schinzel.page_elements

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.route.Parameter
import io.schinzel.page_elements.route.Route
import io.schinzel.page_elements.route.findRoutes
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class InitWeb(pagePackage: String, apiPackage: String) {
    init {
        val javalin = Javalin.create { config ->
            config.staticFiles.add("/site", Location.CLASSPATH)
        }

        // Find all page routes
        val pageRoutes = findRoutes(pagePackage)
        // Find all api routes
        val apiRoutes = findRoutes(apiPackage)

        // Add all routes to Javalin
        (pageRoutes + apiRoutes).forEach { route: Route ->
            // Print route
            route.toString().println()
            val handler = { ctx: Context ->
                // If no arguments, use default constructor
                val routeInstance = if (route.parameters.isEmpty()) {
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
                if (routeInstance is IPage) {
                    val response = routeInstance.getHtml()
                    ctx.html(response)
                }
                if (routeInstance is IApi) {
                    val response = routeInstance.getData()
                    ctx.json(response)
                }
            }

            // Register both GET and POST handlers for the same path
            javalin.get(route.getPath(), handler)
            javalin.post(route.getPath(), handler)
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
}