package io.schinzel.page_elements.web.test_routes.pages.page_with_arguments

import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class MyPage(
    private val myInt: Int,
    private val myString: String,
    private val myBoolean: Boolean
) : IPageRoute {
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