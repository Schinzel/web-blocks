package io.schinzel.page_elements.web.routes

/**
 * The purpose of this interface is to handle the response of a request
 */
interface IRoute {
    fun getResponse(): Any

    fun getPath(): String {
        return RouteDescriptorRegistry
            .getRouteDescriptor(this::class)
            .getRoutePath(this::class)
    }
}


