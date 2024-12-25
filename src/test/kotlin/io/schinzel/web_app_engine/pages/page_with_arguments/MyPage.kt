package io.schinzel.web_app_engine.pages.page_with_arguments

import io.schinzel.web_app_engine.route_registry.processors.IWebPage

class MyPage(val myInt: Int, val myString: String, val myBoolean: Boolean) : IWebPage {
    override fun getResponse(): Any {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
               <meta charset="UTF-8">
               <title>My Page</title>
            </head>
            <body>
               <h1>My Page</h1>
               <p>myInt: $myInt</p>
               <p>myString: $myString</p>
               <p>myBoolean: $myBoolean</p>
            </body>
            </html>
        """.trimIndent()
    }
}