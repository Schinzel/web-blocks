package io.schinzel.page_elements.web.response_handlers

import dev.turingcomplete.textcaseconverter.StandardTextCases
import java.io.File
import kotlin.reflect.KClass

object ResponseHandlerUtil {

    /**
     * Removes suffixes from a class name and converts it to kebab case
     */
    fun removeSuffixesAndToKebabCase(
        clazz: KClass<*>,
        suffixesToRemove: List<String>
    ): String {
        val className = clazz.simpleName!!
        val classNameWithoutSuffixes = removeSuffixes(className, suffixesToRemove)
        return toKebabCase(classNameWithoutSuffixes)
    }

    /**
     * Removes the suffixes from a string
     */
    fun removeSuffixes(string: String, suffixesToRemove: List<String>): String {
        return suffixesToRemove.fold(string) { acc, suffix ->
            if (acc.lowercase().endsWith(suffix.lowercase()))
                acc.substring(0, acc.length - suffix.length)
            else acc
        }
    }

    /**
     * Converts a string to kebab case
     */
    fun toKebabCase(string: String): String {
        return StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, string)
    }


    /**
     * Returns the relative path of a class relative to the endpoint package.
     * And replaces "." with "/". and "_" with "-".
     *
     * For example if the endpoint package is "io.schinzel.samples.component" a
     * and the class resides in "io.schinzel.samples.component.settings.address"
     * this function returns "pages/settings/address"
     */
    fun getRelativePath(endpointPackage: String, clazz: KClass<out IResponseHandler>): String =
        clazz.java
            .packageName
            .removePrefix(endpointPackage)
            .removePrefix(".")
            .replace(".", File.separatorChar.toString())
            .replace("_", "-")
}