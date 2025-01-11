package io.schinzel.page_elements.web.request_handler

import io.javalin.Javalin
import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.errors.ErrorPages
import io.schinzel.page_elements.web.response_handlers.ReturnTypeEnum

fun Javalin.setUpErrorHandling(webAppConfig: WebAppConfig): Javalin {
    this
        .exception(Exception::class.java) { e, ctx ->
            when (getReturnType()) {
                ReturnTypeEnum.JSON -> {
                    throw Exception("Not implemented")
                }

                ReturnTypeEnum.HTML -> {
                    ErrorPages(webAppConfig.webRootClass, webAppConfig.environment)
                        .getErrorPage(ctx.status().code)
                        .printlnWithPrefix("******** Error page ********")
                    throw Exception("Not implemented")
                }
            }
        }
        // Counterintuitive, but actually catches all 400-HTTP status errors (404, 401, 403, etc),
        // i.e. all client errors
        .error(404) { ctx ->
            val errorPageHtml = ErrorPages(webAppConfig.webRootClass, webAppConfig.environment)
                .getErrorPage(404)
            ctx.html(errorPageHtml)
        }
    return this
}


private fun getReturnType(): ReturnTypeEnum {
    return ReturnTypeEnum.HTML
}