package io.schinzel.sample.pages.landing

import io.schinzel.web_blocks.web.routes.IPageRoute

@Suppress("unused")
class LandingPage : IPageRoute {
    override suspend fun getResponse(): String {
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