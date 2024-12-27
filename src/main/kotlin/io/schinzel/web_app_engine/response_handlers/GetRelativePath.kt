package io.schinzel.web_app_engine.response_handlers

import io.schinzel.web_app_engine.response_handlers.response_handlers.IResponseHandler
import kotlin.reflect.KClass

fun getRelativePath(endpointPackage: String, clazz: KClass<out IResponseHandler>): String =
    clazz.java
        .packageName
        .removePrefix(endpointPackage)
        .removePrefix(".")
        .replace(".", "/")
        .replace("_", "-")