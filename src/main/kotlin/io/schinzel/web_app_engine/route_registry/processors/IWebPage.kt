package io.schinzel.web_app_engine.route_registry.processors

import io.schinzel.web_app_engine.route_registry.IRouteGenerator
import io.schinzel.web_app_engine.route_registry.ReturnTypeEnum
import kotlin.reflect.KClass

interface IWebPage : IRequestProcessor

class WebPageRouteGenerator : IRouteGenerator<IWebPage> {
    override fun getPath(relativePath: String, clazz: KClass<out IWebPage>): String {
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        return if (pagePathWithoutPages == "landing") "/" else pagePathWithoutPages
    }

    override fun getTypeName() = "WebPage"
    override fun getReturnType() = ReturnTypeEnum.HTML
}