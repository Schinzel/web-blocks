package io.schinzel.web

import io.schinzel.basicutils.thrower.Thrower
import io.schinzel.web.request_handler.log.ConsoleLogger
import io.schinzel.web.request_handler.log.ILogger
import java.time.ZoneId

/**
 * The purpose of this class is to hold configuration settings for the web app.
 * @param routesPackage The package where the response handlers are located.
 * @param port The port the web app will listen to. Default value is 5555.
 * @param logger The logger to use. Default value is a console logger.
 * @param localTimezone The timezone to use. Default value is "Europe/Stockholm".
 * @param prettyFormatHtml If the html should be pretty formatted. Default value is true.
 * For production environment set this should be false.
 */
data class WebAppConfig(
    val routesPackage: String,
    val port: Int = 5555,
    val logger: ILogger = ConsoleLogger(prettyPrint = true),
    val localTimezone: String = "Europe/Stockholm",
    val prettyFormatHtml: Boolean = true,
) {
    init {
        Thrower.throwIfFalse(port in 1..65535)
            .message("Incorrect port '$port'. Port must be between 1 and 65535.")
        Thrower.throwIfFalse(isValidTimezone(localTimezone))
            .message("'$localTimezone' is not a valid timezone")
        Thrower.throwIfFalse(isValidPackage(routesPackage))
            .message("'$routesPackage' is not a valid package")

    }

    companion object {
        private fun isValidPackage(packageName: String): Boolean =
            ClassLoader.getSystemClassLoader()
                .definedPackages
                .any { it.name == packageName }


        private fun isValidTimezone(timezone: String): Boolean =
            try {
                ZoneId.of(timezone)
                true
            } catch (e: Exception) {
                false
            }
    }
}