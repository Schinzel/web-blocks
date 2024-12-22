package io.schinzel.page_elements.endpoint.path

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

class ApiPath(relativePath: String, clazz: KClass<*>) : IPath {
    override val path: String

    init {
        // Remove the optional  "Api" suffix from the class name
        val nameWithOutApiSuffix = clazz.simpleName!!.removeSuffix("Api")
        // Convert the class name from pascal case to kebab case
        val classNameKebabCase = StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, nameWithOutApiSuffix)
        // Create the path with the prefix "api" and the relative path and the class name
        path = "api/$relativePath/$classNameKebabCase"
    }

}