package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_registry.processors.IResponseHandler
import io.schinzel.web_app_engine.route_registry.processors.ReturnTypeEnum

fun sendResponse(ctx: Context, routeClassInstance: IResponseHandler, log: Log) {
    val returnType = routeClassInstance.getReturnType()
    val response = routeClassInstance.getResponse()
    when (returnType) {
        ReturnTypeEnum.HTML -> ctx.html(response as String)
        ReturnTypeEnum.JSON -> {
            ctx.json(response)
            log.responseLog.response = response
        }
    }
}