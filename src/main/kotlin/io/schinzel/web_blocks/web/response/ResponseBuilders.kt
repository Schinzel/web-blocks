package io.schinzel.web_blocks.web.response

/**
 * The purpose of this function is to create an HtmlResponse with convenient syntax.
 */
fun html(
    content: String,
    status: Int = 200,
    headers: Map<String, String> = emptyMap(),
) = HtmlContentResponse(content, status, headers)

/**
 * The purpose of this function is to create a JsonResponse with convenient syntax.
 */
fun jsonSuccess(
    data: Any,
    status: Int = 200,
    headers: Map<String, String> = emptyMap(),
): JsonSuccessResponse = JsonSuccessResponse(data, status, headers)

/**
 * The purpose of this function is to create a JsonErrorResponse
 * with the provided error information.
 */
fun jsonError(
    code: String,
    message: String,
    status: Int = 500,
    details: Map<String, Any>? = null,
    headers: Map<String, String> = emptyMap(),
): JsonErrorResponse =
    JsonErrorResponse(
        JsonErrorResponse.ErrorData(code, message, details),
        status,
        headers,
    )
