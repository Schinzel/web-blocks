package io.schinzel.web_blocks.web.routes

import io.schinzel.web_blocks.web.routes.annotations.RouteTypeEnum

/**
 * The purpose of this enum is to represent the different return types
 * that WebBlock routes can produce.
 *
 * Written by Claude Sonnet 4
 */
enum class ReturnTypeEnum {
    HTML,
    JSON,
    ;

    companion object {
        /**
         * Get the return type based on route annotation.
         *
         * @param routeType The route type from annotation detection
         * @return The corresponding return type
         */
        fun getReturnTypeFromRouteType(routeType: RouteTypeEnum): ReturnTypeEnum =
            when (routeType) {
                RouteTypeEnum.PAGE -> HTML
                RouteTypeEnum.API -> JSON
                RouteTypeEnum.PAGE_BLOCK -> HTML
                RouteTypeEnum.PAGE_BLOCK_API -> JSON
                RouteTypeEnum.PAGE_API -> JSON
                RouteTypeEnum.UNKNOWN -> throw IllegalArgumentException(
                    "Cannot determine return type for unknown route type",
                )
            }
    }

    /**
     * Get the Content-Type header value
     * for this return type.
     *
     * @return String representing the Content-Type header value
     */
    fun getContentType(): String =
        when (this) {
            HTML -> "text/html"
            JSON -> "application/json"
        }
}
