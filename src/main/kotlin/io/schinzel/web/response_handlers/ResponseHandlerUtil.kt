package io.schinzel.web.response_handlers

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

object ResponseHandlerUtil {

    fun removeSuffixesAndToKebabCase(
        clazz: KClass<*>,
        suffixesToRemove: List<String>
    ): String {
        val className = clazz.simpleName!!
        val classNameWithoutSuffixes = removeSuffixes(className, suffixesToRemove)
        return toKebabCase(classNameWithoutSuffixes)
    }

    fun removeSuffixes(className: String, suffixesToRemove: List<String>): String {
        return suffixesToRemove.fold(className) { acc, suffix ->
            if (acc.lowercase().endsWith(suffix.lowercase()))
                acc.substring(0, acc.length - suffix.length)
            else acc
        }
    }

    fun toKebabCase(className: String): String {
        return StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, className)
    }


    fun getRelativePath(endpointPackage: String, clazz: KClass<out IResponseHandler>): String =
        clazz.java
            .packageName
            .removePrefix(endpointPackage)
            .removePrefix(".")
            .replace(".", "/")
            .replace("_", "-")
}