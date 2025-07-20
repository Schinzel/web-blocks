package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.route_descriptors.RouteDescriptorRegistry

/**
 * The purpose of this interface is to provide common functionality for all routes
 */
interface IRoute {
    fun getPath(): String =
        RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
}
