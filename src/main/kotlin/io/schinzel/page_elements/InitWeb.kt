package io.schinzel.page_elements

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.staticfiles.Location
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.route.Parameter
import io.schinzel.page_elements.route.Route
import io.schinzel.page_elements.route.findRoutes
import io.schinzel.page_elements.route.log.ErrorLog
import io.schinzel.page_elements.route.log.ILogger
import io.schinzel.page_elements.route.log.Log
import io.schinzel.page_elements.route.log.PrettyConsoleLogger
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

class InitWeb(
    pagePackage: String,
    apiPackage: String,
    private val localTimezone: String = "Europe/Stockholm",
    private val logger: ILogger = PrettyConsoleLogger(),
) {

    init {
        val javalin = Javalin.create { config ->
            // Serve static files at /static/*
            config.staticFiles.add {
                it.directory = "/site"
                it.location = Location.CLASSPATH
                it.hostedPath = "/static"  // Add this prefix
            }
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
            // Check if route has arguments
            val hasArguments = route.parameters.isNotEmpty()
            // If has arguments
            if (hasArguments) {
                // Create path with parameters
                val pathWithParams = route.parameters.fold(route.getPath()) { path, param ->
                    "$path/{${param.name}}"
                }
                // Register both GET and POST handlers for the same path
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
            val log =
                Log(localTimeZone = localTimezone, routeType = route.getType(), httpMethod = ctx.method().toString())
            val startTime = System.currentTimeMillis()
            try {
                log.requestLog.path = route.getPath()
                val hasNoArguments = route.parameters.isEmpty()
                // Create instance of route class
                val routeClassInstance: IWebResponse = when {
                    // If no arguments, use no-argument constructor
                    hasNoArguments -> route.clazz.createInstance()
                    else -> {
                        // If arguments, use constructor with arguments
                        val arguments: Map<String, String> = getArguments(route.parameters, ctx)
                        log.requestLog.arguments = arguments
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
                sendResponse(ctx, routeClassInstance, log)
                log.responseLog.statusCode = ctx.statusCode()
                log.executionTimeInMs = System.currentTimeMillis() - startTime
            } catch (e: Exception) {
                log.executionTimeInMs = System.currentTimeMillis() - startTime
                log.errorLog = ErrorLog(e)
            }
            logger.log(log)
        }
    }


    private fun sendResponse(ctx: Context, routeClassInstance: IWebResponse, log: Log) {
        val response = routeClassInstance.getResponse()
        when (routeClassInstance) {
            is IWebPage -> ctx.html(response as String)
            is IApi -> {
                ctx.json(response)
                log.responseLog.response = response
            }

            else -> throw IllegalStateException("Class ${routeClassInstance.javaClass.simpleName} must implement IPage or IApi")
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
            arg.name to value
        }
    }
}