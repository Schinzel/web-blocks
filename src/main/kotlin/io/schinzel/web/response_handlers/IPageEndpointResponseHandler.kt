package io.schinzel.web.response_handlers

import kotlin.reflect.KClass

interface IPageEndpointResponseHandler : IResponseHandler


class PageEndpointResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IPageEndpointResponseHandler> {

    override fun getRoutePath(clazz: KClass<out IPageEndpointResponseHandler>): String {
        val relativePath = ResponseHandlerUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = ResponseHandlerUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("PageEndpoint", "Endpoint"))
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "WebPageEndpoint"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}