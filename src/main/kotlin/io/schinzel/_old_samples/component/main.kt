package io.schinzel._old_samples.component

import io.schinzel.page_elements.web.Environment
import io.schinzel.page_elements.web.AbstractWebApp

/**
 * The purpose of this class is to start a demo of the component framework.
 * See README.md for links to use with this demo.
 */
fun main() {
    MyWebApp().start()
}

class MyWebApp : AbstractWebApp() {
    // Required configuration
    override val port: Int = 5555
    override val environment = Environment.DEVELOPMENT
}
