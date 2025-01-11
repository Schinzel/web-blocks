package io.schinzel.page_elements.web.request_handler

import io.javalin.Javalin
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.errors.ErrorPages
import io.schinzel.page_elements.web.response_handlers.ReturnTypeEnum

/**
 * TO DO:
 * - Determine if it is a HTML or JSON response.
 *      - Look at the prefix of the path
 * - Add JSON responses
 * - Add logging
 */

fun Javalin.setUpErrorHandling(webAppConfig: WebAppConfig): Javalin {
    this
        .exception(Exception::class.java) { e, ctx ->
            when (getReturnType()) {
                ReturnTypeEnum.JSON -> {
                    throw Exception("Not implemented")
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml = ErrorPages(webAppConfig.webRootClass, webAppConfig.environment)
                        .getErrorPage(500)
                    ctx.html(errorPageHtml)
                }
            }
        }
        .error(404) { ctx ->
            when (getReturnType()) {
                ReturnTypeEnum.JSON -> {
                    throw Exception("Not implemented")
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml = ErrorPages(webAppConfig.webRootClass, webAppConfig.environment)
                        .getErrorPage(404)
                    ctx.html(errorPageHtml)
                }
            }
        }
    return this
}



private fun getReturnType(): ReturnTypeEnum {
    return ReturnTypeEnum.HTML
}