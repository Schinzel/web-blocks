package io.schinzel.web_blocks.web.test_routes.pages.simple_page

import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class MySimplePage : IPageRoute {
    override suspend fun getResponse(): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
               <meta charset="UTF-8">
               <title>Hello</title>
            </head>
            <body>
               <h1>Hello world!</h1>
            </body>
            </html>
        """.trimIndent()
    }
}