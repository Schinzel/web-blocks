package io.schinzel.page_elements.web

import io.schinzel.page_elements.web.request_handler.log.NoLogger

object WebAppConfigUtil {
    fun get(routesPackage: String): WebAppConfig {
        val randomPort = (49152..65535).random()
        return WebAppConfig(
            webRootPackage = routesPackage,
            port = randomPort,
            logger = NoLogger(),
            prettyFormatHtml = false,
            printStartupMessages = false,
        )

    }
}