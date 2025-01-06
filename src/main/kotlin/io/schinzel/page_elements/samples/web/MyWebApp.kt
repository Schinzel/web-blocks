package io.schinzel.page_elements.samples.web

import io.schinzel.page_elements.web.WebApp

class MyWebApp : WebApp() {
    // Required configuration
    override val port: Int = 8080
}

fun main() {
    MyWebApp().start()
}