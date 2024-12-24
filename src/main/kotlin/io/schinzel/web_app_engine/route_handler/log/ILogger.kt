package io.schinzel.web_app_engine.route_handler.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.schinzel.basic_utils_kotlin.println

interface ILogger {
    fun log(logEntry: Log)
}

class CompactConsoleLogger : ILogger {
    override fun log(logEntry: Log) {
        JsonMapper.prettyMapper
            .writeValueAsString(logEntry)
            .println()
    }
}

class PrettyConsoleLogger : ILogger {
    override fun log(logEntry: Log) {
        JsonMapper.prettyMapper
            .writeValueAsString(logEntry)
            .println()
    }
}


object JsonMapper {
    val prettyMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
        enable(SerializationFeature.INDENT_OUTPUT)
    }
    val noIndentMapper = ObjectMapper().apply {
        registerModule(KotlinModule.Builder().build())
    }

}

