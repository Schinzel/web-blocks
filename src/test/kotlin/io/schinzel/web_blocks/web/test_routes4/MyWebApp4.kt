package io.schinzel.web_blocks.web.test_routes4

import io.schinzel.web_blocks.web.WebBlocksApp
import io.schinzel.web_blocks.web.request_handler.log.NoLogger

class MyWebApp4 : WebBlocksApp() {
    override val port: Int = (49152..65535).random()
    override val logger = NoLogger()
    override val prettyFormatHtml = false
    override val printStartupMessages = false
}
