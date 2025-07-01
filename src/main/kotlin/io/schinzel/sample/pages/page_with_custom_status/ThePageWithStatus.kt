package io.schinzel.sample.pages.page_with_custom_status

import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IPageRoute

/**
 * The purpose of this class is to demonstrate using HtmlResponseBuilder
 * to set custom status codes in a page response.
 *
 * Written by Claude Sonnet 4
 */
@Suppress("unused")
class ThePageWithStatus : IPageRoute {
    override suspend fun getResponse(): WebBlockResponse {
        return HtmlResponse.builder()
            .setContent("""
                |<!DOCTYPE html>
                |<html lang="en">
                |<head>
                |    <meta charset="UTF-8">
                |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                |    <title>Resource Created</title>
                |</head>
                |<body>
                |    <h1>Resource Created Successfully</h1>
                |    <p>This page demonstrates using a custom status code (201) with HtmlResponseBuilder.</p>
                |    <p>Check the response headers in your browser's developer tools to see the 201 status.</p>
                |</body>
                |</html>
            """.trimMargin())
            .setStatus(201)
            .build()
    }
}