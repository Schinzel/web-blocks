package io.schinzel.page_elements.web.errors

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.component.template_engine.file_reader.FileReaderUtil
import io.schinzel.page_elements.web.WebAppConfig
import java.io.File

object Error {
    lateinit var errorDir: String
    lateinit var environment: Environment

    fun init(errorDir: String, environment: Environment) {
        this.errorDir = errorDir
        this.environment = environment
    }

    fun getErrorPage(errorCode: Int): String {
        val fileName = "$errorCode.html"
        val html = TemplateProcessor(errorDir)
            .addData("error_code", errorCode)
            .addData("error_message", "An error occurred")
            .addData("error_description", "An error occurred")
            .processTemplate(fileName)
        return html
    }
}

fun main() {
    val webAppConfig = WebAppConfig(
        routesPackage = "io.schinzel.page_elements.samples.web"
    )
    val pathToErrorDir = FileReaderUtil
        .fromPackageToPath(webAppConfig.routesPackage) +
            File.separatorChar + "errors"
    pathToErrorDir.printlnWithPrefix("pathToErrorDir")

}


sealed class Environment {
    object DEVELOPMENT : Environment()
    object STAGING : Environment()
    object PRODUCTION : Environment()

    class Custom(val name: String) : Environment()
}

// User adds their environment
val ACCEPTANCE = Environment.Custom("ACCEPTANCE")

// Or define a proper object
object ACCEPTANCE_V2 : Environment()
