package io.schinzel.page_elements.web.errors

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements.component.template_engine.TemplateProcessor
import io.schinzel.page_elements.component.template_engine.file_reader.FileReaderFactory
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

/**
 * If environment is not dev{
 *      if an environment-error-code-file does exists
 *          return environment error-code-file
 *      if environment-default-file does exist
 *          return environment default-file
 * }
 *  if development-error-code-file does exist
 *      return development-error-code-file
 *  if development-default-file does exist
 *      return development-default-file
 * return hard coded default error page
 *
 */
class ErrorPages(
    private val webRootClass: Any,
    private val environment: Environment
) {

    fun getErrorPage(errorCode: Int): String {
        val fileName = getFileNameV2(webRootClass, environment, errorCode)
            ?: return "<h1>An error occurred</h1>"
        fileName.printlnWithPrefix("Error page file name")
        val html = TemplateProcessor(webRootClass)
            .addData("error_code", errorCode)
            .addData("error_message", "An error occurred")
            .addData("error_description", "An error occurred")
            .processTemplate(fileName)
        return html
    }

    companion object {
        /**
         * @return The path to the error page for the given error code relative
         * the the web root. For example: "errors/404.html" or "errors/default.html"
         * or "errors/production/404.html"
         *
         * If not event the default error page is found, null is returned.
         */
        fun getFileNameV2(webRootClass: Any, environment: Environment, errorCode: Int): String? {
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
                    // Return environment default-file
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

    /*
    fun getFileName(errorCode: Int): String {
        if (environment.isDevelopment()) {
            return getDevFileName(errorCode)
        }
        val fileReader = FileReaderFactory.create(webRootClass)
        val environmentDirectoryExists = fileReader
            .getFile("errors/${environment.getEnvironmentName()}")
            .exists()
        if (!environmentDirectoryExists) {
            return getDevFileName(errorCode)
        }
        return ""
    }

    private fun getDevFileName(errorCode: Int): String {
        val fileNameErrorCodeFile = "errors/$errorCode.html"
        val errorCodeFileExists = FileReaderFactory.create(webRootClass)
            .getFile(fileNameErrorCodeFile)
            .exists()
        return when (errorCodeFileExists) {
            true -> fileNameErrorCodeFile
            false -> "errors/default.html"
        }
    }

    private fun getErrorFileName(errorCode: Int, environment: Environment): String {
        val path = when (environment.isDevelopment()) {
            true -> "errors"
            false -> "errors/${environment.getEnvironmentName()}"
        }
        val fileNameErrorCodeFile = "$path/$errorCode.html"
        val errorCodeFileExists = FileReaderFactory.create(webRootClass)
            .getFile(fileNameErrorCodeFile)
            .exists()
        return when (errorCodeFileExists) {
            true -> fileNameErrorCodeFile
            false -> "$path/default.html"
        }
    }*/
}
