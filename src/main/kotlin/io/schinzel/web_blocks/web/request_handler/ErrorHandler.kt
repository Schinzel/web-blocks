package io.schinzel.web_blocks.web.request_handler

import io.javalin.Javalin
import io.schinzel.basicutils.RandomUtil
import io.schinzel.web_blocks.web.WebAppConfig
import io.schinzel.web_blocks.web.errors.ErrorPage
import io.schinzel.web_blocks.web.routes.ReturnTypeEnum

/**
 * TO DO:
 * - Add logging
 *    - Want that unique id that is displayed in the response to the client also in the log
 */

fun Javalin.setUpErrorHandling(webAppConfig: WebAppConfig): Javalin {
    this
        .exception(Exception::class.java) { e, ctx ->
            val errorId: String = RandomUtil.getRandomString(12)
            when (getReturnType(ctx.path())) {
                ReturnTypeEnum.JSON -> {
                    val response =
                        ApiResponse.Error(
                            message = e.message ?: "Internal server error",
                            errorId = errorId,
                        )
                    ctx.json(response)
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml =
                        ErrorPage(webAppConfig.webRootClass, webAppConfig.environment)
                            .addData("errorMessage", e.message ?: "Unknown error")
                            .addData("errorId", errorId)
                            .getErrorPage(500)
                    ctx.html(errorPageHtml)
                }
            }
        }.error(404) { ctx ->
            val errorId: String = RandomUtil.getRandomString(12)
            when (getReturnType(ctx.path())) {
                ReturnTypeEnum.JSON -> {
                    val response =
                        ApiResponse.Error(
                            message = "API or Page API route not found: '${ctx.path()}'",
                            errorId = errorId,
                        )
                    ctx.json(response)
                }

                ReturnTypeEnum.HTML -> {
                    val errorPageHtml =
                        ErrorPage(webAppConfig.webRootClass, webAppConfig.environment)
                            .addData("errorMessage", "Page route not found: '${ctx.path()}'")
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
