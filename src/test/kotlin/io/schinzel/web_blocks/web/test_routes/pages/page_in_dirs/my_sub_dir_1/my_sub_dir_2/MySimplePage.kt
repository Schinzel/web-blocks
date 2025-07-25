package io.schinzel.web_blocks.web.test_routes.pages.page_in_dirs.my_sub_dir_1.my_sub_dir_2

import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Suppress("unused")
@Page
class MySimplePage : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse =
        html(
            """
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
            """.trimIndent(),
        )
}
