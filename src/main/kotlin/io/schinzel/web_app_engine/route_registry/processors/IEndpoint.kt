package io.schinzel.web_app_engine.route_registry.processors


interface IEndpoint {
    fun getResponse(): Any

    fun getReturnType(): ReturnTypeEnum
}


enum class ReturnTypeEnum { HTML, JSON }
