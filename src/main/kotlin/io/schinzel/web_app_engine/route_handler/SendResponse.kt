package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.route_registry.response_handlers.IResponseHandler
import io.schinzel.web_app_engine.route_registry.response_handlers.ReturnTypeEnum

fun sendResponse(ctx: Context, responseHandler: IResponseHandler, log: Log) {
    val returnType = responseHandler.getReturnType()
    val response: Any = responseHandler.getResponse()
    when (returnType) {
        ReturnTypeEnum.HTML -> ctx.html(response as String)
        ReturnTypeEnum.JSON -> {
            ctx.json(response)
            log.responseLog.response = response
        }
    }
}