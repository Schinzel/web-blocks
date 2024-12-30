package io.schinzel.sample

import io.schinzel.web_app_engine.InitWebApp

fun main() {
    InitWebApp(
        endpointPackage = "io.schinzel.sample",
        port = 5555
    )
}
