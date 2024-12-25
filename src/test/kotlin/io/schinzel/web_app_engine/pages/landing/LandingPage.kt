package io.schinzel.web_app_engine.pages.landing

import io.schinzel.web_app_engine.route_registry.processors.IWebPage

@Suppress("unused")
class LandingPage : IWebPage {
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