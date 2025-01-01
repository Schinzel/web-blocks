package io.schinzel.samples.web.pages.my_dir.my_page

import io.schinzel.web.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyOtherPage : IPageResponseHandler {
    override fun getResponse(): String {
        return """
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Hello from the other page</title>
           |</head>
           |<body>
           |    <h1>Hello World</h1>
           |</body>
           |</html>
        """.trimMargin()
    }
}