package io.schinzel.pages.template_engine

import io.schinzel.pages.template_engine.file_reader.FileReaderFactory

/**
 * The purpose of this interface is to process a template.
 */
interface ITemplateProcessor {
    fun addData(key: String, value: String): ITemplateProcessor
    fun processTemplate(template: String): String
}

/**
 * This class is used to process a template.
 */
class TemplateProcessor(private val caller: Any) : ITemplateProcessor {
    private val data: MutableMap<String, String> = mutableMapOf()

    override fun addData(key: String, value: String): ITemplateProcessor {
        data[key] = value
        return this
    }

    override fun processTemplate(template: String): String {
        val templateWithIncludeFilesRead = processIncludeFiles(template, caller)
        return applyData(templateWithIncludeFilesRead, data)
    }


    companion object {
        private const val INCLUDE_FILE_START = "{{include:"
        private const val INCLUDE_FILE_END = "}}"
        private const val MAX_INCLUDE_DEPTH = 10
    }

    /**
     * @param content The content of the template file to process.
     * @param caller The class that is calling this class. Used to find the file to read.
     * @param depth The depth of the include file. Used to prevent circular dependencies.
     * @return The file content with include files processed.
     */
    private fun processIncludeFiles(content: String, caller: Any, depth: Int = 0): String {
        if (depth >= MAX_INCLUDE_DEPTH) {
            throw IllegalStateException("Max include depth ($MAX_INCLUDE_DEPTH) exceeded. Possible circular dependency.")
        }
        var processedTemplate = content
        while (true) {
            val startIndex = processedTemplate.indexOf(INCLUDE_FILE_START)
            if (startIndex == -1) break

            val endIndex = processedTemplate.indexOf(INCLUDE_FILE_END, startIndex)
            if (endIndex == -1) break

            val includeFileName = processedTemplate
                .substring(startIndex + INCLUDE_FILE_START.length, endIndex)
                .trim()

            // Read include file
            val includeFileContent = FileReaderFactory.create(includeFileName, caller)
                .getFileContent()
            // Process include file recursively
            val processedContent = processIncludeFiles(includeFileContent, caller, depth + 1)

            processedTemplate = processedTemplate.substring(0, startIndex) +
                    processedContent +
                    processedTemplate.substring(endIndex + INCLUDE_FILE_END.length)
        }
        return processedTemplate
    }

    /**
     * @param template The content of the template file to process.
     * @param data The data to replace placeholders with.
     * @return The file content with placeholders replaced with values from the data map.
     */
    private fun applyData(template: String, data: Map<String, String>): String {
        return data.entries.fold(template) { content, (key, value) ->
            content.replace("{{$key}}", value)
        }
    }
}
