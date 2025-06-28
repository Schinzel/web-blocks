package io.schinzel.page_elements.web.route_mapping

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.page_elements.web.routes.IRoute
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun getConstructorParameters(clazz: KClass<out IRoute>): List<Parameter> {
    // Get constructor parameters using Kotlin reflection
    val constructorParams = clazz.primaryConstructor?.parameters
    return (constructorParams
        ?.map { param ->
            // Convert parameter name to kebab case
            val parameterNameInKebabCase = StandardTextCases.SOFT_CAMEL_CASE
                .convertTo(StandardTextCases.KEBAB_CASE, param.name)
            Parameter(
                name = parameterNameInKebabCase,
                type = param.type
            )
        }
        ?: emptyList())
}