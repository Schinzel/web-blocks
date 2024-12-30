package io.schinzel.pages.template_engine

/**
 * This class is used to render a template.
 * @param fileName The name of the file to process.
 * @param caller The class that is calling this class.
 * Used to find the file to read.
 */
class TemplateRenderer(
    fileName: String,
    caller: Any,
) {
    private val templateReader: FileReader = FileReader(fileName, caller)
    private val templateProcessor = TemplateProcessor(caller)

    fun addData(key: String, value: String): TemplateRenderer {
        templateProcessor.addData(key, value)
        return this
    }

    fun addData(key: String, value: Int): TemplateRenderer {
        return this.addData(key, value.toString())
    }

    fun process(): String {
        val fileContent = templateReader.getFileContent()
        return templateProcessor.processTemplate(fileContent)
    }
}