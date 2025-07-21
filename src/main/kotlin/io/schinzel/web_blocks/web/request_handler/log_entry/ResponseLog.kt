package io.schinzel.web_blocks.web.request_handler.log_entry

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseLog(
    var response: Any? = null,
    var statusCode: Int = -1,
)
