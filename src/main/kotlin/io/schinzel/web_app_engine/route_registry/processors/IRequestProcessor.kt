package io.schinzel.web_app_engine.route_registry.processors


interface IRequestProcessor {
    fun getResponse(): Any
}

