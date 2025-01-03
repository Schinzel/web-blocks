package io.schinzel.web.response_handlers

import kotlin.reflect.KClass

interface IPageResponseHandler : IResponseHandler {
    override fun getResponse(): String
}


class PageResponseHandlerDescriptor(
    private val endpointPackage: String
) : IResponseHandlerDescriptor<IPageResponseHandler> {
    private val systemPaths = listOf("api", "page-api", "static")

    override fun getRoutePath(clazz: KClass<out IPageResponseHandler>): String {
        val relativePath = ResponseHandlerUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val returnPath = if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
        systemPaths.forEach { systemPath ->
            if (returnPath.startsWith(systemPath)) {
                throw IllegalArgumentException("Page path cannot start with '$systemPath'. " +
                        "Page path: '$returnPath'")
            }
        }
        return returnPath
    }

    override fun getTypeName() = "WebPage"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}