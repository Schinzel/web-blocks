package io.schinzel.page_elements.route_handler.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.schinzel.basic_utils_kotlin.println

interface ILogger {
    fun log(log: Log)
}

class CompactConsoleLogger : ILogger {
    override fun log(log: Log) {
        JsonMapper.prettyMapper
            .writeValueAsString(log)
            .println()
    }
}

class PrettyConsoleLogger : ILogger {
    override fun log(log: Log) {
        JsonMapper.prettyMapper
            .writeValueAsString(log)
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

