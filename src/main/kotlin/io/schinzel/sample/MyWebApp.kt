package io.schinzel.sample

import io.schinzel.web_blocks.web.WebBlocksApp

class MyWebApp : WebBlocksApp() {
    override val port: Int = 5555
}

fun main() {
    MyWebApp()
        .start()
}
