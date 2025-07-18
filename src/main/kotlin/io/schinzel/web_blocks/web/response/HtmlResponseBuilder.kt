package io.schinzel.web_blocks.web.response

/**
 * The purpose of this class is to provide a fluent builder for creating HtmlResponse
 * objects that works consistently across all JVM languages.
 *
 * Written by Claude Sonnet 4
 */
class HtmlResponseBuilder {
    private var content: String = ""
    private var status: Int = 200
    private val headers: MutableMap<String, String> = mutableMapOf()

    /**
     * The purpose of this function is to set the HTML content for the response.
     */
    fun setContent(content: String) = apply { this.content = content }

    /**
     * The purpose of this function is to set the HTTP status code.
     */
    fun setStatus(status: Int) = apply { this.status = status }

    /**
     * The purpose of this function is to add a single header to the response.
     */
    fun addHeader(
        key: String,
        value: String,
    ) = apply { headers[key] = value }

    /**
     * The purpose of this function is to add multiple headers to the response.
     */
    fun addHeaders(headers: Map<String, String>) = apply { this.headers.putAll(headers) }

    /**
     * The purpose of this function is to build the final HtmlResponse object.
     */
    fun build(): HtmlResponse {
        require(content.isNotEmpty()) { "HTML content cannot be empty" }
        return HtmlResponse(content, status, headers.toMap())
    }
}
