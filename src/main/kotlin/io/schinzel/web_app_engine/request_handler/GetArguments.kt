package io.schinzel.web_app_engine.request_handler

import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.web_app_engine.response_handler_mapping.Parameter

fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, Any> {
    return parameters.associate { parameter ->
        val contentTypeIsJson = ctx.contentType() == "application/json"
        val postValue = when {
            contentTypeIsJson -> {
                val requestBody = ctx.bodyAsClass<Map<String, Any>>()
                requestBody[parameter.name]?.toString()
            }
            else -> ctx.formParam(parameter.name)
        }
            ?: ctx.queryParam(parameter.name)
            ?: throw IllegalArgumentException("Missing argument '${parameter.name}'")
        val parameterValue = try {
            parameter.convertValue(postValue)
        } catch (e: Exception) {
            throw IllegalArgumentException(
                "Could not convert value '$postValue' " +
                        "to type ${parameter.type} for parameter '${parameter.name}'"
            )
        }

        parameter.name to parameterValue
    }
}