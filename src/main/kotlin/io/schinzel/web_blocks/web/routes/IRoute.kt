package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.IWebBlockResponse

/**
 * The purpose of this interface is to handle the response of a request
 */
interface IRoute {
    suspend fun getResponse(): IWebBlockResponse

    fun getPath(): String =
        RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
}
