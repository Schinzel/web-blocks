package io.schinzel.web_app_engine.route_handler.log

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Log(
    var routeType: String = "",
    var httpMethod: String = "",
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
)

data class ResponseLog(
    var response: Any? = null,
    var statusCode: Int = -1,
)

class ErrorLog(e: Exception) {
    @Suppress("unused")
    val errorMessage: String = e.message ?: ""
    @Suppress("unused")
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

fun main() {
    ZoneId.getAvailableZoneIds().forEach { println(it) }
}