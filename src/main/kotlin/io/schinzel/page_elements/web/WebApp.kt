package io.schinzel.page_elements.web

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.component.template_engine.file_reader.FileReaderUtil
import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger
import io.schinzel.page_elements.web.request_handler.log.ILogger
import java.io.File

abstract class WebApp {
    // Optional configuration with defaults
    open val port: Int = 5555
    open val logger: ILogger = ConsoleLogger(prettyPrint = true)
    open val localTimezone: String = "Europe/Stockholm"
    open val prettyFormatHtml: Boolean = true
    open val printStartupMessages: Boolean = true
    open val sourceDirectory: String = "src/main/kotlin"

    init {
        getSourceRoot(this, sourceDirectory)
            .printlnWithPrefix("WebApp source root")
        /* WebAppConfig(
             routesPackage = "io.schinzel.page_elements.samples.component",
             port = port,
             logger = logger,
             localTimezone = localTimezone,
             prettyFormatHtml = prettyFormatHtml,
             printStartupMessages = printStartupMessages
         )*/
    }

}

fun getSourceRoot(caller: Any, sourceDirectory: String): String {
    val projectDir = File("").absolutePath
    val pathToClass = caller::class.java.packageName.replace('.', File.separatorChar)
    return "$projectDir/$sourceDirectory/$pathToClass"
}


class MyWebApp : WebApp() {
    // Required configuration
    override val port: Int = 8080
    override val prettyFormatHtml: Boolean = true
    override val localTimezone: String = "Europe/Stockholm"
}

fun main() {
    MyWebApp()
}