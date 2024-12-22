package io.schinzel.page_elements.route_handler

import io.javalin.http.Context
import io.schinzel.page_elements.route.log.Log
import io.schinzel.page_elements.web_response.IApi
import io.schinzel.page_elements.web_response.IWebPage
import io.schinzel.page_elements.web_response.IWebResponse

fun sendResponse(ctx: Context, routeClassInstance: IWebResponse, log: Log) {
    val response = routeClassInstance.getResponse()
    when (routeClassInstance) {
        is IWebPage -> ctx.html(response as String)
        is IApi -> {
            ctx.json(response)
            log.responseLog.response = response
        }

        else -> throw IllegalStateException("Class ${routeClassInstance.javaClass.simpleName} must implement IPage or IApi")
    }
}