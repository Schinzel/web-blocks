package io.schinzel.web_app_engine.response_handlers.response_handlers

import io.schinzel.web_app_engine.response_handlers.getClassNameAsKebabCase
import io.schinzel.web_app_engine.response_handlers.getRelativePath
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