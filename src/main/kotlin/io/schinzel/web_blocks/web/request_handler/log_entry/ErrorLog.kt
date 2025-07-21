package io.schinzel.web_blocks.web.request_handler.log_entry

import io.schinzel.basicutils.RandomUtil

@Suppress("unused")
class ErrorLog(
    e: Exception,
) {
    // A unique id for this error
    val errorId: String = RandomUtil.getRandomString(12)
    val errorMessage: String = e.message ?: ""
    val stackTrace: List<String> =
        e
            .stackTraceToString()
            .split("\n")
            .take(5)
            .map { it.replace("\t", "") }
}
