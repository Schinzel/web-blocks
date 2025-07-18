package io.schinzel.web_blocks.component.template_engine

import io.schinzel.web_blocks.component.template_engine.file_reader.FileReaderFactory
import io.schinzel.web_blocks.component.template_engine.file_reader.IFileReader

/**
 * This class is used to process a template.
 */
class TemplateProcessor(
    private val fileReader: IFileReader,
) {
    private val data: MutableMap<String, String> = mutableMapOf()

    constructor(caller: Any) : this(FileReaderFactory.create(caller))

    fun addData(
        key: String,
        value: String,
    ): TemplateProcessor {
        data[key] = value
        return this
    }

    fun addData(
        key: String,
        value: Int,
    ): TemplateProcessor = this.addData(key, value.toString())

    fun addDataSet(data: MutableMap<String, String>): TemplateProcessor {
        this.data.putAll(data)
        return this
    }

    fun processTemplate(fileName: String): String {
        val content = fileReader.getFileContent(fileName)
        val templateWithIncludeFilesRead = processIncludeFiles(content)
        return applyData(templateWithIncludeFilesRead, data)
    }

    companion object {
        private const val INCLUDE_FILE_START = "{{include:"
        private const val INCLUDE_FILE_END = "}}"
        private const val MAX_INCLUDE_DEPTH = 10
    }

    /**
     * @param content The content of the template file to process.
     * @param depth The depth of the include file. Used to prevent circular dependencies.
     * @return The file content with include files processed.
     */
    private fun processIncludeFiles(
        content: String,
        depth: Int = 0,
    ): String {
        if (depth >= MAX_INCLUDE_DEPTH) {
            throw IllegalStateException(
                "Max include depth ($MAX_INCLUDE_DEPTH) exceeded. Possible circular dependency.",
            )
        }
        var processedTemplate = content
        while (true) {
            val startIndex = processedTemplate.indexOf(INCLUDE_FILE_START)
            if (startIndex == -1) break

            val endIndex = processedTemplate.indexOf(INCLUDE_FILE_END, startIndex)
            if (endIndex == -1) break

            val includeFileName =
                processedTemplate
                    .substring(startIndex + INCLUDE_FILE_START.length, endIndex)
                    .trim()

            // Read include file
            val includeFileContent = fileReader.getFileContent(includeFileName)
            // Process include file recursively
            val processedContent = processIncludeFiles(includeFileContent, depth + 1)

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
    private fun applyData(
        template: String,
        data: Map<String, String>,
    ): String =
        data.entries.fold(template) { content, (key, value) ->
            content.replace("{{$key}}", value)
        }
}
