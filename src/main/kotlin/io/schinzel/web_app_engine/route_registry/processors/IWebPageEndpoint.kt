package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IEndpointPath
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IWebPageEndpoint : IEndpoint {
    override fun getReturnType() = ReturnTypeEnum.JSON
}


class WebPageEndpointPath : IEndpointPath<IWebPageEndpoint> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IWebPageEndpoint>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebPageEndpoint"
}