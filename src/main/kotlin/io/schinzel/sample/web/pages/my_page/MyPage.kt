package io.schinzel.sample.web.pages.my_page

import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class MyPage : IPageResponseHandler {
    override fun getResponse(): String {
        return """
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Hello World</title>
           |</head>
           |<body>
           |    <h1>Hello World</h1>
           |</body>
           |</html>
        """.trimMargin()
    }
}