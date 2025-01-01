package io.schinzel.web.response_handlers

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

object ResponseHandlerUtil {
    fun getClassNameAsKebabCase(clazz: KClass<*>): String {
        return clazz.simpleName!!
            .removeSuffix("PageEndpoint")
            .removeSuffix("Endpoint")
            .removeSuffix("Api")
            .let { StandardTextCases.PASCAL_CASE.convertTo(StandardTextCases.KEBAB_CASE, it) }
    }

    fun getRelativePath(endpointPackage: String, clazz: KClass<out IResponseHandler>): String =
        clazz.java
            .packageName
            .removePrefix(endpointPackage)
            .removePrefix(".")
            .replace(".", "/")
            .replace("_", "-")
}