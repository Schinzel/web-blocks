package io.schinzel.web_blocks.web.routes.route_descriptors

import dev.turingcomplete.textcaseconverter.StandardTextCases
import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_blocks.web.routes.IRoute
import java.io.File
import kotlin.reflect.KClass

object RouteUtil {
    /**
     * Removes suffixes from a class name and converts it to kebab case
     */
    fun removeSuffixesAndToKebabCase(
        clazz: KClass<*>,
        suffixesToRemove: List<String>,
    ): String {
        val className = clazz.simpleName!!
        val classNameWithoutSuffixes = removeSuffixes(className, suffixesToRemove)
        return toKebabCase(classNameWithoutSuffixes)
    }

    fun removeSuffixesAndToKebabCase(
        clazzSimpleName: String,
        suffixesToRemove: List<String>,
    ): String {
        val classNameWithoutSuffixes = removeSuffixes(clazzSimpleName, suffixesToRemove)
        return toKebabCase(classNameWithoutSuffixes)
    }

    /**
     * Removes the suffixes from a string
     */
    fun removeSuffixes(
        string: String,
        suffixesToRemove: List<String>,
    ): String =
        suffixesToRemove.fold(string) { acc, suffix ->
            if (acc.lowercase().endsWith(suffix.lowercase())) {
                acc.substring(0, acc.length - suffix.length)
            } else {
                acc
            }
        }

    /**
     * Converts a string to kebab case
     */
    fun toKebabCase(string: String): String =
        StandardTextCases.PASCAL_CASE
            .convertTo(StandardTextCases.KEBAB_CASE, string)

    /**
     * Returns the relative path of a class relative to the endpoint package.
     * And replaces "." with "/". and "_" with "-".
     *
     * For example if the endpoint package is "io.schinzel.samples.component" a
     * and the class resides in "io.schinzel.samples.component.settings.address"
     * this function returns "/settings/address"
     */
    fun getRelativePath(
        webRootPackage: String,
        clazz: KClass<out IRoute>,
    ): String = getRelativePath(webRootPackage, clazz.java.packageName)


    fun getRelativePath(
        webRootPackage: String,
        classPackageName: String,
    ): String =
        "/" + classPackageName
            .removePrefix(webRootPackage)
            .removePrefix(".")
            .replace(".", File.separatorChar.toString())
            .replace("_", "-")
}

fun main() {
    RouteUtil
        .getRelativePath("io.schinzel.samples.component", "io.schinzel.samples.component.settings.address")
        .println()
}


