package io.schinzel.web_app_engine.route_registry

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

fun getClassNameAsKebabCase(clazz: KClass<*>): String {
    return clazz.simpleName!!
        .removeSuffix("WebPageEndpoint")
        .removeSuffix("Endpoint")
        .removeSuffix("Api")
        .let { StandardTextCases.PASCAL_CASE.convertTo(StandardTextCases.KEBAB_CASE, it) }
}