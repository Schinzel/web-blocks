package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IRouteGenerator
import kotlin.reflect.KClass

interface IWebPage : IEndpoint {
    override fun getReturnType() = ReturnTypeEnum.HTML
    override fun getResponse(): String

}

class WebPageRouteGenerator : IRouteGenerator<IWebPage> {
    override fun getPath(relativePath: String, clazz: KClass<out IWebPage>): String {
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
}