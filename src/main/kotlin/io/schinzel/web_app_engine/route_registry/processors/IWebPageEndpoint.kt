package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IRouteGenerator
import io.schinzel.web_app_engine.route_registry.ReturnTypeEnum
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import kotlin.reflect.KClass

interface IWebPageEndpoint : IRequestProcessor


class WebPageEndpointRouteGenerator : IRouteGenerator<IWebPageEndpoint> {
    override fun getPath(relativePath: String, clazz: KClass<out IWebPageEndpoint>): String {
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebPageEndpoint"
    override fun getReturnType() = ReturnTypeEnum.JSON
}