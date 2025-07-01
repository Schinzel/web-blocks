package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse
import kotlin.reflect.KClass

interface IPageApiRoute : IRoute {
    override suspend fun getResponse(): WebBlockResponse
}


class PageApiRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<IPageApiRoute> {

    override fun getRoutePath(clazz: KClass<out IPageApiRoute>): String {
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
        val pagePathWithoutPages = relativePath.removePrefix("pages/")
        val classNameKebabCase = RouteUtil
            .removeSuffixesAndToKebabCase(clazz, listOf("Route"))
        return "page-api/$pagePathWithoutPages/$classNameKebabCase"
    }

    override fun getTypeName() = "PageApiRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.JSON
}