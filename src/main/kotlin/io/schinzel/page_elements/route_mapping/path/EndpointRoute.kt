package io.schinzel.page_elements.route_mapping.path

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

class EndpointRoute(basePath: String, clazz: KClass<*>) : IRoute {
    override val path: String

    init {
        val className = clazz.simpleName!!
            // Remove the optional "Api" suffix from the class name
            .removeSuffix("Api")
            // Remove the optional "Endpoint" suffix from the class name
            .removeSuffix("Endpoint")
        // Convert the class name from pascal case to kebab case
        val classNameKebabCase = StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, className)
        // Create the path with the prefix "api" and the relative path and the class name
        path = "api/$basePath/$classNameKebabCase"
    }
}