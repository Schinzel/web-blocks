package io.schinzel.sample.pages.landing

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
@Suppress("unused")
class LandingPage : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse =
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
