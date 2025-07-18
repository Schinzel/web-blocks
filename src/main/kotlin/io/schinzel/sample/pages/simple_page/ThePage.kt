package io.schinzel.sample.pages.simple_page

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
@Suppress("unused")
class ThePage : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse =
        html(
            """
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Hello World</title>
           |</head>
           |<body>
           |    <h1>Hello World</h1>
           |</body>
           |</html>
            """.trimMargin(),
        )
}
