package io.schinzel.web_app_engine.route_mapping

import dev.turingcomplete.textcaseconverter.StandardTextCases
import kotlin.reflect.KClass

interface IRoute {
    val path: String
}

class PageRoute(pagePath: String) : IRoute {
    val pagePathWithoutPages = pagePath.removePrefix("pages/")
    override val path: String = if (pagePathWithoutPages == "landing") {
        "/"
    } else {
        pagePathWithoutPages
    }
}

class EndpointRoute(basePath: String, clazz: KClass<*>) : IRoute {
    override val path: String

    init {
        // Convert the class name from pascal case to kebab case
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        // Create the path with the prefix "api" and the relative path and the class name
        path = "$basePath/$classNameKebabCase"
    }
}

class WebPageEndPoint(basePath: String, clazz: KClass<*>) : IRoute {
    override val path: String

    init {
        val pagePathWithoutPages = basePath.removePrefix("pages/")
        // Convert the class name from pascal case to kebab case
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        // Create the path with the prefix "api" and the relative path and the class name
        path = "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }
}

private fun getClassNameAsKebabCase(clazz: KClass<*>): String {
    return clazz.simpleName!!
        .removeSuffix("WebPageEndpoint")
        .removeSuffix("Endpoint")
        .removeSuffix("Api")
        .let { StandardTextCases.PASCAL_CASE.convertTo(StandardTextCases.KEBAB_CASE, it) }
}