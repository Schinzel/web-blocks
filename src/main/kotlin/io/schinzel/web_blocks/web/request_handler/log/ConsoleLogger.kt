package io.schinzel.web_blocks.web.request_handler.log

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry

class ConsoleLogger(
    private val prettyPrint: Boolean,
) : ILogger {
    override fun log(logEntry: LogEntry) {
        val mapper =
            when (prettyPrint) {
                true -> JsonMapper.prettyMapper
                false -> JsonMapper.noIndentMapper
            }
        mapper
            .writeValueAsString(logEntry)
            .println()
    }
}
