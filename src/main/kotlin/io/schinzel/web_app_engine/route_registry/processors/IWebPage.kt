package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IEndpointPath
import io.schinzel.web_app_engine.route_registry.getRelativePath
import kotlin.reflect.KClass

interface IWebPage : IEndpoint {
    override fun getReturnType() = ReturnTypeEnum.HTML
    override fun getResponse(): String

}

class WebPagePath : IEndpointPath<IWebPage> {
    override fun getPath(endpointPackage: String, clazz: KClass<out IWebPage>): String {
        val relativePath = getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
}