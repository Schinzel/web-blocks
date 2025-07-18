package io.schinzel.web_blocks.web.response

/**
 * The purpose of this class is to provide a fluent builder for creating JsonResponse
 * objects that works consistently across all JVM languages.
 *
 * Written by Claude Sonnet 4
 */
class JsonResponseBuilder {
    private var data: Any? = null
    private var status: Int = 200
    private val headers: MutableMap<String, String> = mutableMapOf()

    /**
     * The purpose of this function is to set the data to be serialized as JSON.
     */
    fun setData(data: Any) = apply { this.data = data }

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
     * The purpose of this function is to build the final JsonResponse object.
     */
    fun build(): JsonResponse {
        requireNotNull(data) { "JSON data cannot be null" }
        return JsonResponse(data!!, status, headers.toMap())
    }
}
