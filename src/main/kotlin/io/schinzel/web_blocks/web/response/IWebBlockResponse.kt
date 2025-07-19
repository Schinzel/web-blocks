package io.schinzel.web_blocks.web.response

/**
 * The purpose of this interface is to define the contract for all web route responses
 * in the framework, providing type safety and consistent response handling.
 */
sealed interface IWebBlockResponse {
    val status: Int get() = 200
    val headers: Map<String, String> get() = emptyMap()
}


/**
 * The purpose of this interface is to group all HTML-related responses
 * including content, redirects, and errors.
 */
sealed interface IHtmlResponse : IWebBlockResponse

/**
 * The purpose of this class is to represent the content of an HTML page
 */
data class HtmlContentResponse(
    val content: String,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap(),
) : IHtmlResponse {
    companion object {
        /**
         * The purpose of this function is to create a new HtmlResponseBuilder.
         */
        @JvmStatic
        fun builder(): HtmlResponseBuilder = HtmlResponseBuilder()
    }
}

/**
 * The purpose of this class is represent a redirect, when requesting an HTML page
 */
data class HtmlRedirectResponse(
    val location: String,
    override val status: Int = 302,
    override val headers: Map<String, String> = emptyMap()
) : IHtmlResponse

/**
 * The purpose of this class is represent an error, when requesting an HTML page
 */
data class HtmlErrorResponse(
    val content: String,
    override val status: Int,
    val errorCode: String? = null,
    override val headers: Map<String, String> = emptyMap()
) : IHtmlResponse


/**
 * The purpose of this interface is to group all JSON-related responses
 */
sealed interface IJsonResponse : IWebBlockResponse

/**
 * The purpose of this class is to represent JSON responses from API routes,
 * preserving automatic serialization of data objects.
 */
data class JsonSuccessResponse(
    val data: Any,
    override val status: Int = 200,
    override val headers: Map<String, String> = emptyMap(),
) : IJsonResponse {
    companion object {
        /**
         * The purpose of this function is to create a new JsonResponseBuilder.
         */
        @JvmStatic
        fun builder(): JsonResponseBuilder = JsonResponseBuilder()
    }
}

/**
 * The purpose of this class is to represent error JSON responses from API routes,
 * providing structured error information to clients.
 */
data class JsonErrorResponse(
    val error: ErrorData,
    override val status: Int = 500,
    override val headers: Map<String, String> = emptyMap(),
) : IJsonResponse {

    /**
     * The purpose of this class is to structure error information
     * in a consistent format for API consumers.
     */
    data class ErrorData(
        val code: String,
        val message: String,
        val details: Map<String, Any>? = null,
    )

    companion object {
        /**
         * The purpose of this function is to create a new JsonErrorResponseBuilder.
         */
        @JvmStatic
        fun builder(): JsonErrorResponseBuilder = JsonErrorResponseBuilder()
    }
}
