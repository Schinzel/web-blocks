package io.schinzel.web_app_engine.pages.page_in_dirs.my_sub_dir_1.my_sub_dir_2

import io.schinzel.web_app_engine.route_registry.processors.IWebPage

@Suppress("unused")
class MySimplePage : IWebPage {
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