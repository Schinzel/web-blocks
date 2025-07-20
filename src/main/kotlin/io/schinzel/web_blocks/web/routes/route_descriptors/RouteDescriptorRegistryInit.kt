package io.schinzel.web_blocks.web.routes.route_descriptors

/**
 * The purpose of this class is to register the route descriptors
 */
class RouteDescriptorRegistryInit(
    endpointPackage: String,
) {
    init {
        RouteDescriptorRegistry
            .registerAnnotation(RouteDescriptorApi(endpointPackage))
            .registerAnnotation(RouteDescriptorPage(endpointPackage))
            .registerAnnotation(RouteDescriptorPageBlock(endpointPackage))
            .registerAnnotation(RouteDescriptorPageBlockApi(endpointPackage))
    }
}
