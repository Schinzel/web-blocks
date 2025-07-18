package io.schinzel.web_blocks.web.response

/**
 * The purpose of this function is to create an HtmlResponse with convenient syntax.
 */
fun html(
    content: String,
    status: Int = 200,
    headers: Map<String, String> = emptyMap(),
) = HtmlResponse(content, status, headers)

/**
 * The purpose of this function is to create a JsonResponse with convenient syntax.
 */
fun json(
    data: Any,
    status: Int = 200,
    headers: Map<String, String> = emptyMap(),
) = JsonResponse(data, status, headers)
