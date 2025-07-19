package io.schinzel.sample.pages.java_style_page

import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

/**
 * The purpose of this class is to demonstrate builder usage
 * that Java developers would find familiar.
 *
 * Written by Claude Sonnet 4
 */
@Page
@Suppress("unused")
class JavaStylePage : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse =
        HtmlContentResponse
            .builder()
            .setContent(generateContent())
            .setStatus(200)
            .addHeaders(getDefaultHeaders())
            .build()

    private fun generateContent(): String =
        """
            |<!DOCTYPE html>
            |<html lang="en">
            |<head>
            |    <meta charset="UTF-8">
            |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
            |    <title>Java-Friendly Example</title>
            |</head>
            |<body>
            |    <h1>Java-Friendly Example</h1>
            |    <p>This page demonstrates the builder pattern that works identically across all JVM languages:</p>
            |    <ul>
            |        <li><strong>Java</strong> - No need for named parameters or default values</li>
            |        <li><strong>Kotlin</strong> - Can use builders for complex cases, convenience functions for simple ones</li>
            |        <li><strong>Scala</strong> - Fluent method chaining works naturally</li>
            |        <li><strong>Clojure</strong> - Thread-first macro works perfectly with builders</li>
            |    </ul>
            |    <p>The response includes custom headers set using the addHeaders() method.</p>
            |</body>
            |</html>
        """.trimMargin()

    private fun getDefaultHeaders(): Map<String, String> =
        mapOf(
            "X-Framework" to "web-blocks",
            "X-Language-Friendly" to "true",
            "X-Builder-Pattern" to "demonstrated",
        )
}
