package io.schinzel.web_blocks.web.request_handler.log

import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry

interface ILogger {
    fun log(logEntry: LogEntry)
}
