package io.schinzel.web.response_handlers.response_handlers

import io.schinzel.web.response_handlers.getRelativePath
import kotlin.reflect.KClass

interface IPageResponseHandler : IResponseHandler {
    override fun getResponse(): String
}


class PageResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IPageResponseHandler> {

    override fun getPath(clazz: KClass<out IPageResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}