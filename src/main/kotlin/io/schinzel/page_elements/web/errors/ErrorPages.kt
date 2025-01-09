package io.schinzel.page_elements.web.errors

import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.web.Environment

/**
 * One folder for each environment. In each folder, there are error pages for that environment.
 * The error pages are named by the error code. For example, 404.html.
 *
 * Each folder has a default error page, default.html. If the error page for an error code is not found in the
 * environment folder, the default error page is used.
 *
 * The root folder contains the default error pages which are used if there
 * is no directory for the current environment.
 */
class ErrorPages(
    private val webRootClass: Any,
    private val environment: Environment
) {

    fun getErrorPage(errorCode: Int): String {
        val fileName = "errors/$errorCode.html"
        val html = TemplateProcessor(webRootClass)
            .addData("error_code", errorCode)
            .addData("error_message", "An error occurred")
            .addData("error_description", "An error occurred")
            .processTemplate(fileName)
        return html
    }

/*    private fun getFileName(errorCode: Int): String {
        if (environment.isDevelopment()) {
            val nameTemplateErrorFile = "errors/$errorCode.html"
            // If the file does not exist, return a default error page
            val nameTemplateErrorFile = "errors/default.html"
            // If default error page does not exist, return a default error page

        } else {
            val environmentName = environment.getEnvironmentName()
            val nameTemplateErrorFile = "errors/$environmentName/$errorCode.html"

        }

    }*/
}


