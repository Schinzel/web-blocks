package io.schinzel.web.pages.landing

import io.schinzel.web.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class LandingPage : IPageResponseHandler {
    override fun getResponse(): String {
        return """
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
        """.trimIndent()
    }
}