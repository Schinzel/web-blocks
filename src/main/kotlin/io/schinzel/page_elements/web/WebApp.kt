package io.schinzel.page_elements.web

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.samples.web.MyWebApp
import io.schinzel.page_elements.web.errors.ErrorPages
import io.schinzel.page_elements.web.request_handler.log.ConsoleLogger
import io.schinzel.page_elements.web.request_handler.log.ILogger
import java.io.File

abstract class WebApp {
    // Optional configuration with defaults
    //open val port: Int = 5555.also { println("Port 1: $it") }
    open val port: Int by lazy { 5555 }
    open val logger: ILogger = ConsoleLogger(prettyPrint = true)
    open val localTimezone: String = "Europe/Stockholm"
    open val prettyFormatHtml: Boolean = true
    open val printStartupMessages: Boolean = true
    open val sourceDirectory: String = "src/main/kotlin"

    fun start() {
        val webRootPackage = this::class.java.packageName
            .printlnWithPrefix("Routes package")
        val webRootPath = getSourceRoot(this, sourceDirectory)
            .printlnWithPrefix("WebApp source root")

        this::class.java.simpleName.printlnWithPrefix("WebApp class")
        ErrorPages(this)
            .getErrorPage(404)
            .println()

        val webAppConfig = WebAppConfig(
            webRootPackage = webRootPackage,
            webRootPath = webRootPath,
            webRootClass = this,
            port = port,
            logger = logger,
            localTimezone = localTimezone,
            prettyFormatHtml = prettyFormatHtml,
            printStartupMessages = printStartupMessages
        )
        InitWebApp(webAppConfig)
    }
}

fun getSourceRoot(caller: Any, sourceDirectory: String): String {
    // For example: /Users/schinzel/code/page-elements-kotlin
    val projectDir = File("").absolutePath
    // For example: io/schinzel/page_elements/samples/component
    val pathToClass = caller::class.java.packageName
        .replace('.', File.separatorChar)
    return "$projectDir/$sourceDirectory/$pathToClass"
}


