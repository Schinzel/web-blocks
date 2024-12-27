package io.schinzel.web_app_engine.pages.simple_page

import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class MySimplePage : IPageResponseHandler {
    override fun getResponse(): String {
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