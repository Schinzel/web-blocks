package io.schinzel.page_elements.route.path

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

class ApiPath(relativePath: String, clazz: KClass<*>) : IPath {
    override val path: String

    init {
        // Remove the optional  "Api" suffix from the class name
        val nameWithOutApiSuffix = clazz.simpleName!!.removeSuffix("Api")
        val classNameKebabCase = StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, nameWithOutApiSuffix)
        path = "api/$relativePath/$classNameKebabCase"
    }

}