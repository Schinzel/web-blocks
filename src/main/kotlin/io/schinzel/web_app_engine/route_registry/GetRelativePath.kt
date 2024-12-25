package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.IRequestProcessor
import kotlin.reflect.KClass

fun getRelativePath(basePackage: String, clazz: KClass<out IRequestProcessor>): String =
    clazz.java
        .packageName
        .removePrefix(basePackage)
        .removePrefix(".")
        .replace(".", "/")
        .replace("_", "-")