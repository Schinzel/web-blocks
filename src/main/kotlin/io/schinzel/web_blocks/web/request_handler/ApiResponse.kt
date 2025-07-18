package io.schinzel.web_blocks.web.request_handler

sealed class ApiResponse {
    data class Success(
        val success: Boolean = true,
        val message: Any? = null,
    ) : ApiResponse()

    data class Error(
        val success: Boolean = false,
        val message: String,
        val errorId: String,
    ) : ApiResponse()
}
