package io.schinzel.web_blocks.web.request_handler.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.schinzel.basic_utils_kotlin.println

interface ILogger {
    fun log(logEntry: LogEntry)
}

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

class NoLogger : ILogger {
    override fun log(logEntry: LogEntry) {
        // Do nothing
    }
}

object JsonMapper {
    val prettyMapper =
        ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    val noIndentMapper =
        ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
        }
}
