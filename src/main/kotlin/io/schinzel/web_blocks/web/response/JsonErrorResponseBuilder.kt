package io.schinzel.web_blocks.web.response

/**
 * The purpose of this class is to provide a fluent builder for creating
 * JsonErrorResponse instances, enabling step-by-step construction
 * that works well with Java and other JVM languages.
 *
 * Written by Claude Opus 4
 */
class JsonErrorResponseBuilder {
    // Error fields
    private var code: String = "UNKNOWN_ERROR"
    private var message: String = "An unknown error occurred"
    private var details: Map<String, Any>? = null

    // Response fields
    private var status: Int = 500
    private var headers: MutableMap<String, String> = mutableMapOf()

    /**
     * Sets the error code for this error response.
     * @param code The error code (e.g., "VALIDATION_ERROR", "NOT_FOUND")
     * @return This builder for method chaining
     */
    fun withCode(code: String): JsonErrorResponseBuilder {
        this.code = code
        return this
    }

    /**
     * Sets the error message for this error response.
     * @param message Human-readable error message
     * @return This builder for method chaining
     */
    fun withMessage(message: String): JsonErrorResponseBuilder {
        this.message = message
        return this
    }

    /**
     * Sets additional error details for this error response.
     * @param details Map of additional error information
     * @return This builder for method chaining
     */
    fun withDetails(details: Map<String, Any>): JsonErrorResponseBuilder {
        this.details = details
        return this
    }

    /**
     * Sets the HTTP status code for this error response.
     * @param status HTTP status code (default: 500)
     * @return This builder for method chaining
     */
    fun withStatus(status: Int): JsonErrorResponseBuilder {
        this.status = status
        return this
    }

    /**
     * Adds a single header to this error response.
     * @param name Header name
     * @param value Header value
     * @return This builder for method chaining
     */
    fun withHeader(
        name: String,
        value: String,
    ): JsonErrorResponseBuilder {
        this.headers[name] = value
        return this
    }

    /**
     * Sets all headers for this error response.
     * @param headers Map of headers
     * @return This builder for method chaining
     */
    fun withHeaders(headers: Map<String, String>): JsonErrorResponseBuilder {
        this.headers = headers.toMutableMap()
        return this
    }

    /**
     * Builds the JsonErrorResponse with the configured values.
     * @return A new JsonErrorResponse instance
     */
    fun build(): JsonErrorResponse =
        JsonErrorResponse(
            error =
                JsonErrorResponse.ErrorData(
                    code = code,
                    message = message,
                    details = details,
                ),
            status = status,
            headers = headers.toMap(), // Return immutable copy
        )
}
