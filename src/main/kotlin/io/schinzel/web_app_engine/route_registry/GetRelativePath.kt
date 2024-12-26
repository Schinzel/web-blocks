package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.IEndpoint
import kotlin.reflect.KClass

fun getRelativePath(basePackage: String, clazz: KClass<out IEndpoint>): String =
    clazz.java
        .packageName
        .removePrefix(basePackage)
        .removePrefix(".")
        .replace(".", "/")
        .replace("_", "-")