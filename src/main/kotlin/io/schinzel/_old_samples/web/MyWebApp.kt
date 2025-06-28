package io.schinzel._old_samples.web

import io.schinzel.page_elements.web.AbstractWebApp

class MyWebApp() : AbstractWebApp() {
    override val port: Int = 5555
}

fun main() {
    MyWebApp()
        .start()
}