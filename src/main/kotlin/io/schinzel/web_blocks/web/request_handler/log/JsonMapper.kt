package io.schinzel.web_blocks.web.request_handler.log

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule

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
