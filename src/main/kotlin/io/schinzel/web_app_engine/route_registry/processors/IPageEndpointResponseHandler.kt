package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IResponseHandlerDescriptor
import io.schinzel.web_app_engine.route_registry.getClassNameAsKebabCase
import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IPageEndpointResponseHandler : IResponseHandler {
    override fun getReturnType() = ReturnTypeEnum.JSON
}


class PageEndpointResponseHandlerDescriptor : IResponseHandlerDescriptor<IPageEndpointResponseHandler> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IPageEndpointResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = getClassNameAsKebabCase(clazz)
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebPageEndpoint"
}