package io.schinzel.sample

import io.schinzel.web_blocks.web.WebBlocksApp

class SampleWebApp : WebBlocksApp() {
    override val port: Int = 5555
}

fun main() {
    SampleWebApp()
        .start()
}
