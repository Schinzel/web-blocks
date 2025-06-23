package io.schinzel.samples.new_sample.pages.landing

import io.schinzel.page_elements.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class ThePage : IPageResponseHandler {
    override fun getResponse(): String {
        return """
           |<!DOCTYPE html>
           |<html lang="en">
           |<head>
           |    <meta charset="UTF-8">
           |    <meta name="viewport" content="width=device-width, initial-scale=1.0">
           |    <title>Landing Page</title>
           |</head>
           |<body>
           |    <h1>Landing Page</h1>
           |</body>
           |</html>
        """.trimMargin()
    }
}