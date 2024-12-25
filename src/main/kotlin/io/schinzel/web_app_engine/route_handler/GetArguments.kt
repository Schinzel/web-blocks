package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_mapping.Parameter

fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, String> {
    return parameters.associate { arg ->
        // If the argument was not path parameter, try the request body
        val postValue = ctx.formParam(arg.name)
        // If the argument was not in the request body, try query parameter
            ?: ctx.queryParam(arg.name)
            ?: ""
        arg.name to postValue
    }
}