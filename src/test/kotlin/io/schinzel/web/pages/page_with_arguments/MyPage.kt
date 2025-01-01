package io.schinzel.web.pages.page_with_arguments

import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyPage(
    private val myInt: Int,
    private val myString: String,
    private val myBoolean: Boolean
) : IPageResponseHandler {
    override fun getResponse(): String {
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