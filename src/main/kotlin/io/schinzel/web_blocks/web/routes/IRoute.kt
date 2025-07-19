package io.schinzel.web_blocks.web.routes

/**
 * The purpose of this interface is to provide common functionality for all routes
 */
interface IRoute {
    fun getPath(): String =
        RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
}
