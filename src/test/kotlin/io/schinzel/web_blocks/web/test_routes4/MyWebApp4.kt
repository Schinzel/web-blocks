package io.schinzel.web_blocks.web.test_routes4

import io.schinzel.web_blocks.web.AbstractWebApp
import io.schinzel.web_blocks.web.request_handler.log.NoLogger

class MyWebApp4 : AbstractWebApp() {
    override val port: Int = (49152..65535).random()
    override val logger = NoLogger()
    override val prettyFormatHtml = false
    override val printStartupMessages = false
}
