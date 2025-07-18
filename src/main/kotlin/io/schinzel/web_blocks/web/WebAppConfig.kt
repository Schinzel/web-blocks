package io.schinzel.web_blocks.web

import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web_blocks.web.request_handler.log.ConsoleLogger
import io.schinzel.web_blocks.web.request_handler.log.ILogger
import java.time.ZoneId

/**
 * The purpose of this class is to hold configuration settings for the web app.
 * @param port The port the web app will listen to. Default value is 5555.
 * @param logger The logger to use. Default value is a console logger.
 * @param localTimezone The timezone to use. Default value is "Europe/Stockholm".
 * @param prettyFormatHtml If the html should be pretty formatted. Default value is true.
 * For production environment set this should be false.
 * @param printStartupMessages If startup messages should be printed. Default value is true.
 */
data class WebAppConfig(
    val webRootClass: Any,
    val port: Int = 5555,
    val logger: ILogger = ConsoleLogger(prettyPrint = true),
    val localTimezone: String = "Europe/Stockholm",
    val prettyFormatHtml: Boolean = true,
    val printStartupMessages: Boolean = true,
    val environment: Environment = Environment.DEVELOPMENT,
) {
    val webRootPackage: String = webRootClass::class.java.packageName

    init {
        Thrower
            .throwIfFalse(port in 1..65535)
            .message("Incorrect port '$port'. Port must be between 1 and 65535.")
        Thrower
            .throwIfFalse(isValidTimezone(localTimezone))
            .message("'$localTimezone' is not a valid timezone")
        Thrower
            .throwIfFalse(isValidPackage(webRootPackage))
            .message("'$webRootPackage' is not a valid package")
    }

    companion object {
        fun isValidPackage(packageName: String): Boolean {
            val path = packageName.replace('.', '/')
            return ClassLoader
                .getSystemClassLoader()
                .getResources(path)
                .hasMoreElements()
        }

        private fun isValidTimezone(timezone: String): Boolean =
            try {
                ZoneId.of(timezone)
                true
            } catch (_: Exception) {
                false
            }
    }
}
