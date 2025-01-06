package io.schinzel.page_elements.web.errors

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.web.WebAppConfig
import java.io.File

class ErrorPages(private val webRootClass: Any) {

    fun getErrorPage(errorCode: Int): String {
        val fileName = "errors/$errorCode.html"
        val html = TemplateProcessor(webRootClass)
            .addData("error_code", errorCode)
            .addData("error_message", "An error occurred")
            .addData("error_description", "An error occurred")
            .processTemplate(fileName)
        return html
    }
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
