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

    /**
     * Check if the given response type is valid for this route type.
     *
     * @param response The response to validate
     * @return true if the response type is valid for this route type
     */
    fun isValidResponseType(response: IWebBlockResponse): Boolean =
        when (this) {
            PAGE -> response is IHtmlResponse
            API -> response is IJsonResponse
            PAGE_BLOCK -> response is IHtmlResponse
            PAGE_BLOCK_API -> response is IJsonResponse
            PAGE_API -> response is IJsonResponse
            UNKNOWN -> false
        }

    /**
     * Get the expected response type name for this route type.
     *
     * @return The expected response type name
     */
    fun getExpectedResponseType(): String =
        when (this) {
            PAGE -> "HtmlResponse"
            API -> "JsonResponse"
            PAGE_BLOCK -> "HtmlResponse"
            PAGE_BLOCK_API -> "JsonResponse"
            PAGE_API -> "JsonResponse"
            UNKNOWN -> "UnknownResponse"
        }
}
