package io.schinzel.page_elements.web.test_routes2

import io.schinzel.page_elements.web.AbstractWebApp
import io.schinzel.page_elements.web.request_handler.log.NoLogger

class MyWebApp2 : AbstractWebApp() {
    override val port: Int = (49152..65535).random()
    override val logger = NoLogger()
    override val prettyFormatHtml = false
    override val printStartupMessages = false
}