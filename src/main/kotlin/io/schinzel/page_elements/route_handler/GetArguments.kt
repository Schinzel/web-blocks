package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.route_mapping.Parameter

fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, String> {
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