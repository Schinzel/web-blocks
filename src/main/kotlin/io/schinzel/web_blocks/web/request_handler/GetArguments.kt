package io.schinzel.web_blocks.web.request_handler

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.javalin.http.Context
import io.schinzel.web_blocks.web.route_mapping.Parameter
import java.net.URLDecoder

/**
 * @param parameters The parameters as defined by the route. I.e. the parameter names
 * are in kebab case.
 * @param ctx The Javalin context
 * @param requestBody The pre-read request body to avoid consuming it multiple times
 * @param isMultipart Whether this is a multipart form data request
 * @return A map with the argument names as keys and the argument values as values
 */
fun getArguments(
    parameters: List<Parameter>,
    ctx: Context,
    requestBody: String,
    isMultipart: Boolean,
): Map<String, Any> {
    // Parse the request body based on content type
    val bodyParams: Map<String, String> =
        when {
            isMultipart -> emptyMap() // Multipart will be handled via ctx.formParam
            requestBody.isNotEmpty() &&
                ctx.contentType()?.contains(
                    "application/json",
                ) == true -> parseJsonBody(requestBody)
            requestBody.isNotEmpty() &&
                ctx.contentType()?.contains(
                    "application/x-www-form-urlencoded",
                ) == true -> parseFormBody(requestBody)
            else -> emptyMap()
        }

    return parameters.associate { parameter ->
        val parameterNameInKebabCase = parameter.name

        // Get the parameter value with fallback priority:
        // 1. Body parameters (JSON or form data)
        // 2. Form parameters (for multipart data)
        // 3. Query parameters
        val parameterValueAsString: String =
            bodyParams[parameterNameInKebabCase]
                ?: (if (isMultipart) ctx.formParam(parameterNameInKebabCase) else null)
                ?: ctx.queryParam(parameterNameInKebabCase)
                ?: throw IllegalArgumentException("Missing argument '${parameter.name}'")

        // Convert the parameter value to the correct type
        val parameterValue =
            try {
                parameter.convertValue(parameterValueAsString)
            } catch (_: Exception) {
                throw IllegalArgumentException(
                    "Could not convert value '$parameterValueAsString' " +
                        "to type ${parameter.type} for parameter '${parameter.name}'",
                )
            }

        // Convert the parameter name to camel case
        val parameterNameInCamelCase =
            StandardTextCases.KEBAB_CASE
                .convertTo(StandardTextCases.SOFT_CAMEL_CASE, parameterNameInKebabCase)
        parameterNameInCamelCase to parameterValue
    }
}

/**
 * Parse JSON request body into a map of parameter names to values
 */
private fun parseJsonBody(requestBody: String): Map<String, String> =
    try {
        val objectMapper: ObjectMapper = jacksonObjectMapper()
        val typeRef = object : TypeReference<Map<String, Any>>() {}
        val jsonMap = objectMapper.readValue(requestBody, typeRef)
        jsonMap.mapValues { (_, value) ->
            value.toString()
        }
    } catch (_: Exception) {
        emptyMap()
    }

/**
 * Parse form data request body into a map of parameter names to values
 * Handles application/x-www-form-urlencoded format
 */
private fun parseFormBody(requestBody: String): Map<String, String> =
    try {
        if (requestBody.isEmpty()) {
            emptyMap()
        } else {
            // Split by & and then by = to get key-value pairs
            requestBody
                .split("&")
                .mapNotNull { pair ->
                    val parts = pair.split("=", limit = 2)
                    if (parts.size == 2) {
                        URLDecoder.decode(parts[0], "UTF-8") to URLDecoder.decode(parts[1], "UTF-8")
                    } else {
                        null
                    }
                }.toMap()
        }
    } catch (_: Exception) {
        emptyMap()
    }
