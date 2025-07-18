package io.schinzel.sample

import io.schinzel.web_blocks.web.AbstractWebApp

class MyWebApp : AbstractWebApp() {
    override val port: Int = 5555
}

fun main() {
    MyWebApp()
        .start()
}
