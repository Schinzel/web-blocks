package io.schinzel.web.response_handlers

import kotlin.reflect.KClass

interface IPageResponseHandler : IResponseHandler {
    override fun getResponse(): String
}


class PageResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IPageResponseHandler> {
    override val reservedStartOfPaths: Set<String> = setOf("page", "page-api")

    override fun getRoutePath(clazz: KClass<out IPageResponseHandler>): String {
        val relativePath = ResponseHandlerUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}