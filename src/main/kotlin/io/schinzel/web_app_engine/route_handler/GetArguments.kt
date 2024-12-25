package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_mapping.Parameter

fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, Any?> {
    return parameters.associate { parameter ->
        // If the argument was not path parameter, try the request body
        val postValue = ctx.formParam(parameter.name)
        // If the argument was not in the request body, try query parameter
            ?: ctx.queryParam(parameter.name)
            ?: ""

        parameter.name to parameter.convertValue(postValue)
    }
}