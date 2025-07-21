package io.schinzel.web_blocks.web.request_handler.log_entry

import com.fasterxml.jackson.annotation.JsonInclude
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
    var requestTimeUtc: String = TimeProvider.nowUtc(),
    val localTimeZone: String,
    var requestTimeLocalTimezone: String = TimeProvider.now(localTimeZone),
    var executionTimeInMs: Long = -1,
    val requestLog: RequestLog = RequestLog(),
    val responseLog: ResponseLog = ResponseLog(),
    var errorLog: ErrorLog? = null,
) {

    private object TimeProvider {
        fun nowUtc(): String = now("UTC")

        fun now(timeZone: String): String =
            LocalDateTime
                .now(ZoneId.of(timeZone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}
