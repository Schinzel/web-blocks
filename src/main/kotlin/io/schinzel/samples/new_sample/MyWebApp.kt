package io.schinzel.samples.new_sample

import io.schinzel.page_elements.web.AbstractWebApp

class MyWebApp() : AbstractWebApp() {
    override val port: Int = 5555
}

fun main() {
    MyWebApp()
        .start()
}