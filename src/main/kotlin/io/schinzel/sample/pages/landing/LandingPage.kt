package io.schinzel.sample.pages.landing

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Page
@Suppress("unused")
class LandingPage : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse =
        html(
            """
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Landing Page</title>
           |</head>
           |<body>
           |    <h1>Landing Page</h1>
           |</body>
           |</html>
            """.trimMargin(),
        )
}
