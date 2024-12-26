package io.schinzel.web_app_engine.route_registry

import io.schinzel.web_app_engine.route_registry.processors.IEndpoint
import kotlin.reflect.KClass

fun getRelativePath(endpointPackage: String, clazz: KClass<out IEndpoint>): String =
    clazz.java
        .packageName
        .removePrefix(endpointPackage)
        .removePrefix(".")
        .replace(".", "/")
        .replace("_", "-")