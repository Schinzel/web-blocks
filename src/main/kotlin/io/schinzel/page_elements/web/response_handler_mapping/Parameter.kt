package io.schinzel.page_elements.web.response_handler_mapping

import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

data class Parameter(
    val name: String,
    val type: KType
) {
    fun convertValue(value: String): Any {
        return when (type) {
            Int::class.starProjectedType -> value.toInt()
            Long::class.starProjectedType -> value.toLong()
            Double::class.starProjectedType -> value.toDouble()
            Float::class.starProjectedType -> value.toFloat()
            Boolean::class.starProjectedType -> value.toBoolean()
            else -> value
        }
    }
}
