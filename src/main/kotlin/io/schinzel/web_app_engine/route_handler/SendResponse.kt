package io.schinzel.web_app_engine.route_handler

import io.javalin.http.Context
import io.schinzel.web_app_engine.route_handler.log.Log
import io.schinzel.web_app_engine.IEndpoint
import io.schinzel.web_app_engine.IWebPage
import io.schinzel.web_app_engine.IRequestProcessor
import io.schinzel.web_app_engine.IWebPageEndpoint

fun sendResponse(ctx: Context, routeClassInstance: IRequestProcessor, log: Log) {
    val response = routeClassInstance.getResponse()

    when (routeClassInstance) {
        is IWebPage -> ctx.html(response as String)
        is IEndpoint, is IWebPageEndpoint -> {
            ctx.json(response)
            log.responseLog.response = response
        }

        else -> throw IllegalStateException("Class ${routeClassInstance.javaClass.simpleName} must implement IPage or IApi")
    }
}