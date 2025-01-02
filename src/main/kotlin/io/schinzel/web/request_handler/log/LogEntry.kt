package io.schinzel.web.request_handler.log

import com.fasterxml.jackson.annotation.JsonInclude
import io.schinzel.basicutils.RandomUtil
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * The purpose of this class is to represent a log entry for a request and response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class LogEntry(
    var routeType: String = "",
    var httpMethod: String = "",
    var requestBody: String = "",
    var requestTimeUtc: String = TimeProvider.nowUtc(),
    val localTimeZone: String,
    var requestTimeLocalTimezone: String = TimeProvider.now(localTimeZone),
    var executionTimeInMs: Long = -1,
    val requestLog: RequestLog = RequestLog(),
    val responseLog: ResponseLog = ResponseLog(),
    var errorLog: ErrorLog? = null,
)

data class RequestLog(
    var path: String = "",
    var arguments: Map<String, Any?> = mapOf(),
    var requestBody: String = "",
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ResponseLog(
    var response: Any? = null,
    var statusCode: Int = -1,
)

@Suppress("unused")
class ErrorLog(e: Exception) {
    // A unique id for this error
    val errorId: String = RandomUtil.getRandomString(12)
    val errorMessage: String = e.message ?: ""
    val stackTrace: List<String> = e.stackTraceToString()
        .split("\n")
        .take(5)
        .map { it.replace("\t", "") }
}


object TimeProvider {
    fun nowUtc(): String = now("UTC")

    fun now(timeZone: String): String {
        return LocalDateTime
            .now(ZoneId.of(timeZone))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
