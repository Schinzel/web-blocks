package io.schinzel.page_elements.samples.component

import io.schinzel.page_elements.web.InitWebApp
import io.schinzel.page_elements.web.WebApp
import io.schinzel.page_elements.web.WebAppConfig
import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger

/**
 * The purpose of this class is to start a demo of the component framework.
 * See README.md for links to use with this demo.
 */
fun main() {
    MyWebApp().start()
}

class MyWebApp : WebApp() {
    // Required configuration
    override val port: Int = 5555
}
