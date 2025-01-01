package io.schinzel.component.template_engine

import io.schinzel.component.template_engine.file_reader.FileReaderFactory

/**
 * This class is used to render a template.
 * @param fileName The name of the file to process.
 * @param caller The class that is calling this class.
 * Used to find the file to read.
 */
class TemplateRenderer(
    private val fileName: String,
    private val caller: Any,
) {
    private val templateProcessor = TemplateProcessor(caller)

    fun addData(key: String, value: String): TemplateRenderer {
        templateProcessor.addData(key, value)
        return this
    }

    fun addData(key: String, value: Int): TemplateRenderer {
        return this.addData(key, value.toString())
    }

    fun process(): String {
        val fileContent = FileReaderFactory.create(fileName, caller)
            .getFileContent()
        return templateProcessor.processTemplate(fileContent)
    }
}