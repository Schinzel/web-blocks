package io.schinzel.page_elements.web.errors

import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.component.template_engine.file_reader.FileReaderFactory
import io.schinzel.page_elements.web.Environment

/**
 * The purpose of this class is to return an error page.
 */
class ErrorPage(
    private val webRootClass: Any,
    private val environment: Environment
) {
    private val data: MutableMap<String, String> = mutableMapOf()

    fun addData(key: String, value: String): ErrorPage {
        data[key] = value
        return this
    }

    fun addData(key: String, value: Int): ErrorPage =
        this.addData(key, value.toString())


    fun getErrorPage(errorCode: Int): String {
        val fileName = getFileName(webRootClass, environment, errorCode)
        // If no error page is found, return default error page
            ?: return getDefaultErrorPage(errorCode)
        return TemplateProcessor(webRootClass)
            .addDataSet(data)
            .addData("errorCode", errorCode)
            .processTemplate(fileName)
    }

    /**
     * @return The default error page.
     */
    private fun getDefaultErrorPage(errorCode: Int): String {
        return TemplateProcessor(this)
            .addDataSet(data)
            .addData("errorCode", errorCode)
            .processTemplate("default_error_page.html")
    }

    companion object {
        /**
         * @return The path to the error page for the given error code relative
         * the the web root. For example: "errors/404.html" or "errors/default.html"
         * or "errors/production/404.html"
         *
         * If no error page is found, null is returned.
         */
        fun getFileName(webRootClass: Any, environment: Environment, errorCode: Int): String? {
            if (true){
                return null
            }
            val fileReader = FileReaderFactory.create(webRootClass)
            // If environment is not development
            if (environment.isNotDevelopment()) {
                // If environment-error-code-file does exists
                val environmentErrorCodeFile = "errors/${environment.getEnvironmentName()}/$errorCode.html"
                if (fileReader.getFile(environmentErrorCodeFile).exists()) {
                    // Return environment error-code-file
                    return environmentErrorCodeFile
                }
                // If environment-default-file does exist
                val environmentDefaultFile = "errors/${environment.getEnvironmentName()}/default.html"
                if (fileReader.getFile(environmentDefaultFile).exists()) {
                    // Return environment-default-file
                    return environmentDefaultFile
                }
            }
            // If development-error-code-file does exist
            val developmentErrorCodeFile = "errors/$errorCode.html"
            if (fileReader.getFile(developmentErrorCodeFile).exists()) {
                // Return development-error-code-file
                return developmentErrorCodeFile
            }
            // If development-default-file does exist
            val developmentDefaultFile = "errors/default.html"
            if (fileReader.getFile(developmentDefaultFile).exists()) {
                // Return development-default-file
                return developmentDefaultFile
            }
            return null
        }
    }
}
