package io.schinzel.web_blocks.web.routes.annotations

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.IJsonResponse
import io.schinzel.web_blocks.web.response.IWebBlockResponse

/**
 * The purpose of this enum is to represent the different types of WebBlock routes.
 *
 * Written by Claude Sonnet 4
 */
enum class RouteTypeEnum {
    PAGE,
    API,
    PAGE_BLOCK,
    PAGE_BLOCK_API,
    PAGE_API,
    UNKNOWN,
    ;

    /**
     * The Content-Type header value for this route type.
     */
    val contentType: String
        get() =
            when (this) {
                PAGE -> "text/html"
                API -> "application/json"
                PAGE_BLOCK -> "text/html"
                PAGE_BLOCK_API -> "application/json"
                PAGE_API -> "application/json"
                UNKNOWN -> "application/octet-stream"
            }

}
