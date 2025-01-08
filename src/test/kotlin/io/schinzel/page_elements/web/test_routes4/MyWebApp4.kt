package io.schinzel.page_elements.web.test_routes4

import io.schinzel.page_elements.web.WebApp
import io.schinzel.page_elements.web.request_handler.log.NoLogger

class MyWebApp4 : WebApp() {
    override val port: Int = (49152..65535).random()
    override val logger = NoLogger()
    override val prettyFormatHtml = false
    override val printStartupMessages = false
}