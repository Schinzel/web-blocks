package io.schinzel.page_elements.web.request_handler

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.schinzel.page_elements.web.response_handler_mapping.Parameter

/**
 * @param parameters The parameters as defined by the route. I.e. the parameter names
 * are in kebab case.
 * @param ctx The Javalin context
 * @return A map with the argument names as keys and the argument values as values
 */
fun getArguments(parameters: List<Parameter>, ctx: Context): Map<String, Any> {
    return parameters.associate { parameter ->
        val parameterNameInKebabCase = parameter.name
        // Establish if the content type is JSON
        val contentTypeIsJson = ctx.contentType() == "application/json"
        // Get the parameter value
        val parameterValueAsString: String = when {
            // If the content type is JSON, get the parameter value from the request body
            contentTypeIsJson -> {
                val requestBody = ctx.bodyAsClass<Map<String, Any>>()
                requestBody[parameterNameInKebabCase]?.toString()
            }
            // If the content type is not JSON, get from a form parameter
            else -> ctx.formParam(parameterNameInKebabCase)
        }
        // Get the parameter value from the query string
            ?: ctx.queryParam(parameterNameInKebabCase)
            // If the parameter value is not found, throw an exception
            ?: throw IllegalArgumentException("Missing argument '${parameter.name}'")
        // Convert the parameter value to the correct type
        val parameterValue = try {
            parameter.convertValue(parameterValueAsString)
        } catch (e: Exception) {
            // If the parameter value cannot be converted to the correct type, throw an exception
            throw IllegalArgumentException(
                "Could not convert value '$parameterValueAsString' " +
                        "to type ${parameter.type} for parameter '${parameter.name}'"
            )
        }
        // Convert the parameter name to camel case
        val parameterNameInCamelCase = StandardTextCases.KEBAB_CASE
            .convertTo(StandardTextCases.SOFT_CAMEL_CASE, parameterNameInKebabCase)
        parameterNameInCamelCase to parameterValue
    }
}