package io.schinzel.web_blocks.web.response

/**
 * The purpose of this interface is to define the contract for all web route responses
 * in the framework, providing type safety and consistent response handling.
 *
 * Written by Claude Sonnet 4
 */
sealed interface WebBlockResponse {
    val status: Int get() = 200
    val headers: Map<String, String> get() = emptyMap()
}

/**
 * The purpose of this class is to represent HTML responses from page routes.
 *
 * Written by Claude Sonnet 4
 */
data class HtmlResponse(
    val content: String,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse {
    companion object {
        /**
         * The purpose of this function is to create a new HtmlResponseBuilder.
         */
        @JvmStatic
        fun builder(): HtmlResponseBuilder = HtmlResponseBuilder()
    }
}

/**
 * The purpose of this class is to represent JSON responses from API routes,
 * preserving automatic serialization of data objects.
 *
 * Written by Claude Sonnet 4
 */
data class JsonResponse(
    val data: Any,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap()
) : WebBlockResponse {
    companion object {
        /**
         * The purpose of this function is to create a new JsonResponseBuilder.
         */
        @JvmStatic
        fun builder(): JsonResponseBuilder = JsonResponseBuilder()
    }
}