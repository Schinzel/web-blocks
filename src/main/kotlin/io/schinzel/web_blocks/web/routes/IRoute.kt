package io.schinzel.web_blocks.web.routes

/**
 * The purpose of this interface is to handle the response of a request
 */
interface IRoute {
    suspend fun getResponse(): Any

    fun getPath(): String {
        return RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
    }
}


