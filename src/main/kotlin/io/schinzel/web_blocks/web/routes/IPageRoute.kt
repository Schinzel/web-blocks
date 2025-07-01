package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse
import kotlin.reflect.KClass

interface IPageRoute : IRoute {
    override suspend fun getResponse(): WebBlockResponse
}


class PageRouteDescriptor(
    private val endpointPackage: String
) : IRouteDescriptor<IPageRoute> {
    private val systemPaths = listOf("api", "page-api", "static")

    override fun getRoutePath(clazz: KClass<out IPageRoute>): String {
        val relativePath = RouteUtil.getRelativePath(endpointPackage, clazz)
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

    override fun getTypeName() = "PageRoute"
    override fun getReturnType(): ReturnTypeEnum = ReturnTypeEnum.HTML
}