package io.schinzel.web_blocks.web.request_handler.log_entry

data class RequestLog(
    var path: String = "",
    var arguments: Map<String, Any?> = mapOf(),
    var requestBody: String = "",
)
