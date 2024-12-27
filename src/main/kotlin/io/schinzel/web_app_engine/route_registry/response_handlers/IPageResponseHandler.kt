package io.schinzel.web_app_engine.route_registry.response_handlers

import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IPageResponseHandler : IResponseHandler {
    override fun getReturnType() = ReturnTypeEnum.HTML
    override fun getResponse(): String


}

class PageResponseHandlerDescriptor : IResponseHandlerDescriptor<IPageResponseHandler> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IPageResponseHandler>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
}