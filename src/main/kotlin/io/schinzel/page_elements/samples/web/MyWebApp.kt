package io.schinzel.page_elements.samples.web

import io.schinzel.page_elements.web.AbstractWebApp

class MyWebApp() : AbstractWebApp() {
    override val port: Int = 5555
}

fun main() {
    MyWebApp()
        .start()
}