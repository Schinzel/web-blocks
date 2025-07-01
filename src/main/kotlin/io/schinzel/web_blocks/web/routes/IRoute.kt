package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.response.WebBlockResponse

/**
 * The purpose of this interface is to handle the response of a request
 */
interface IRoute {
    suspend fun getResponse(): WebBlockResponse

    fun getPath(): String {
        return RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
    }
}


