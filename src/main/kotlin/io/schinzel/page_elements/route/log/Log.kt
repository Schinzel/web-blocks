package io.schinzel.page_elements.route.log

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Log(
    var type: String = "",
    var requestTimeUtc: String = TimeProvider.nowUtc(),
    val localTimeZone: String,
    var requestTimeLocalTimezone: String = TimeProvider.now(localTimeZone),
    var executionTimeInMs: Long = -1,
    val requestLog: RequestLog = RequestLog(),
    val responseLog: ResponseLog = ResponseLog(),
)

data class RequestLog(
    var path: String = "",
    var arguments: Map<String, String> = mapOf(),
)

data class ResponseLog(
    var response: Any? = null,
    var statusCode: Int = -1,
)


object TimeProvider {
    fun nowUtc(): String = this.now("UTC")

    fun now(timeZone: String): String {
        return LocalDateTime
            .now(ZoneId.of(timeZone))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}

fun main() {
    ZoneId.getAvailableZoneIds().forEach { println(it) }
}