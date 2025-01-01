package io.schinzel.web.pages.page_in_dirs.my_sub_dir_1.my_sub_dir_2

import io.schinzel.web.response_handlers.IPageResponseHandler

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
               <h1>Hello sub dir world!</h1>
            </body>
            </html>
        """.trimIndent()
    }
}