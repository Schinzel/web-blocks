package io.schinzel.web_blocks.web.routes.route_descriptors

import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum

/**
 * The purpose of this class is to register the route descriptors
 */
class RouteDescriptorRegistryInit(
    endpointPackage: String,
) {
    init {
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.API,
            RouteDescriptorApi(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE,
            RouteDescriptorPage(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE_BLOCK,
            RouteDescriptorPageBlock(endpointPackage),
        )
        RouteDescriptorRegistry.registerAnnotation(
            RouteTypeEnum.PAGE_BLOCK_API,
            RouteDescriptorPageBlockApi(endpointPackage),
        )
    }
}
