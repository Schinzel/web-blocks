package io.schinzel.web_blocks.web.request_handler.log

import io.schinzel.web_blocks.web.request_handler.log_entry.LogEntry

class NoLogger : ILogger {
    override fun log(logEntry: LogEntry) {
        // Do nothing
    }
}
