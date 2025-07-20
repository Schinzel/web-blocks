package io.schinzel.web_blocks.web.routes

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
