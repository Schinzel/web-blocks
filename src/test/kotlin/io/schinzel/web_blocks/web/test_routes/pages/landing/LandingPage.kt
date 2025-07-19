package io.schinzel.web_blocks.web.test_routes.pages.landing

import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Suppress("unused")
@Page
class LandingPage : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse =
        html(
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
               <meta charset="UTF-8">
               <title>Hello</title>
            </head>
            <body>
               <h1>Hello landing page!</h1>
            </body>
            </html>
            """.trimIndent(),
        )
}
