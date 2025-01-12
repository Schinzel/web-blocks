package io.schinzel.page_elements.web.request_handler

import io.javalin.Javalin
import io.schinzel.basicutils.RandomUtil
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.errors.ErrorPage
import io.schinzel.page_elements.web.response_handlers.ReturnTypeEnum

/**
 * TO DO:
 * - Add JSON responses
 * - Add logging
 *    - Want that unique id that is displayed in the response to the client also in the log
 */

fun Javalin.setUpErrorHandling(webAppConfig: WebAppConfig): Javalin {
    this
        .exception(Exception::class.java) { e, ctx ->
            when (getReturnType(ctx.path())) {
                ReturnTypeEnum.JSON -> {
                    throw Exception("Not implemented")
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml = ErrorPage(webAppConfig.webRootClass, webAppConfig.environment)
                        .addData("errorMessage", e.message ?: "Unknown error")
                        .getErrorPage(500)
                    ctx.html(errorPageHtml)
                }
            }
        }
        .error(404) { ctx ->
            val errorId: String = RandomUtil.getRandomString(12)
            when (getReturnType(ctx.path())) {
                ReturnTypeEnum.JSON -> {
                    throw Exception("Not implemented")
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml = ErrorPage(webAppConfig.webRootClass, webAppConfig.environment)
                        .addData("errorMessage", "Page not found: '${ctx.path()}'")
                        .addData("errorId", errorId)
                        .getErrorPage(404)
                    ctx.html(errorPageHtml)
                }
            }
        }
    return this
}


private fun getReturnType(path: String): ReturnTypeEnum {
    // If the path starts with /api or /page-api, return JSON
    if (path.startsWith("/api") || path.startsWith("/page-api")) {
        return ReturnTypeEnum.JSON
    }
    return ReturnTypeEnum.HTML
}