package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_registry.processors.IRequestProcessor
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_registry.ReturnTypeEnum
import io.schinzel.web_app_engine.route_registry.RouteRegistry

fun sendResponse(ctx: Context, routeClassInstance: IRequestProcessor, log: Log) {
    val returnType = RouteRegistry
        .getGenerator(routeClassInstance::class)
        .getReturnType()
    val response = routeClassInstance.getResponse()
    when (returnType) {
        ReturnTypeEnum.HTML -> ctx.html(response as String)
        ReturnTypeEnum.JSON -> {
            ctx.json(response)
            log.responseLog.response = response
        }
    }
}