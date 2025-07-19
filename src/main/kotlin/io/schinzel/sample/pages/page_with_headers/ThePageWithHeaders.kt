package io.schinzel.sample.pages.page_with_headers

import io.schinzel.web_blocks.web.response.HtmlResponse
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

/**
 * The purpose of this class is to demonstrate using HtmlResponseBuilder
 * to add custom headers to a page response.
 *
 * Written by Claude Sonnet 4
 */
@Page
@Suppress("unused")
class ThePageWithHeaders : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse =
        HtmlResponse
            .builder()
            .setContent(
                """
                |<!DOCTYPE html>
                |<html lang="en">
                |<head>
                |    <meta charset="UTF-8">
                |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                |    <title>Advanced Page with Headers</title>
                |</head>
                |<body>
                |    <h1>Advanced Page</h1>
                |    <p>This page demonstrates using HtmlResponseBuilder to add custom headers.</p>
                |    <p>Check the response headers in your browser's developer tools to see:</p>
                |    <ul>
                |        <li>X-Page-Type: advanced</li>
                |        <li>Cache-Control: no-cache</li>
                |        <li>X-Framework: web-blocks</li>
                |    </ul>
                |</body>
                |</html>
                """.trimMargin(),
            ).setStatus(200)
            .addHeader("X-Page-Type", "advanced")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("X-Framework", "web-blocks")
            .build()
}
