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
            // Serve static files from the classpath
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
            // Create handler
            val handler = createRouteHandler(route)
            // Register both GET and POST handlers for the same path
            javalin.get(route.getPath(), handler)
            javalin.post(route.getPath(), handler)
            val hasArguments = route.parameters.isNotEmpty()
            if (hasArguments) {
                val pathWithParams = route.parameters.fold(route.getPath()) { path, param ->
                    "$path/{${param.name}}"
                }
                javalin.get(pathWithParams, handler)
                javalin.post(pathWithParams, handler)
            }

        }
        // Start server
        javalin.start(5555)

        "*".repeat(30).println()
        "Project started".println()
        "*".repeat(30).println()
    }


    private fun createRouteHandler(route: Route): (Context) -> Unit {
        return { ctx: Context ->
            val hasNoArguments = route.parameters.isEmpty()
            // Create instance of route class
            val routeClassInstance = when {
                // If no arguments, use no-argument constructor
                hasNoArguments -> route.clazz.createInstance()
                else -> {
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
            }

            when (routeClassInstance) {
                is IPage -> {
                    val response = routeClassInstance.getHtml()
                    ctx.html(response)
                }

                is IApi -> {
                    val response = routeClassInstance.getData()
                    ctx.json(response)
                }

                else -> {
                    throw IllegalStateException("Class ${route.clazz.simpleName} must implement IPage or IApi")
                }
            }
        }
    }


    private fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, String> {
        return parameters.associate { arg ->
            val value = try {
                // Try to find the argument as a path parameter
                ctx.pathParam(arg.name)
            } catch (e: IllegalArgumentException) {
                // If the argument was not path parameter, try the request body
                val postValue = ctx.formParam(arg.name)
                // If the argument was not in the request body, try query parameter
                postValue ?: ctx.queryParam(arg.name) ?: ""
            }

            "Value for ${arg.name} is $value".println()
            arg.name to value
        }
    }
}