package io.schinzel.web_app_engine.response_handler_mapping

import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

data class Parameter(
    val name: String,
    val type: KType
) {
    fun convertValue(value: String): Any? {
        if (value.isEmpty()) return null
        return try {
            when (type) {
                Int::class.starProjectedType -> value.toInt()
                Long::class.starProjectedType -> value.toLong()
                Double::class.starProjectedType -> value.toDouble()
                Float::class.starProjectedType -> value.toFloat()
                Boolean::class.starProjectedType -> value.toBoolean()
                else -> value
            }
        } catch (e: Exception) {
            throw IllegalArgumentException("Could not convert value '$value' to type $type")
        }
    }
}
