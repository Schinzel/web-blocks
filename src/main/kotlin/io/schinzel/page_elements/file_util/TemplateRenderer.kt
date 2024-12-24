package io.schinzel.page_elements.file_util

class TemplateRenderer(
    fileName: String,
    caller: Any,
) {
    private val templateReader = FileFileReader(fileName, caller)
    private val templateProcessor = TemplateProcessor()

    fun addData(key: String, value: String): TemplateRenderer {
        templateProcessor.data[key] = value
        return this
    }

    fun process(): String {
        return TemplateEngine(templateReader, templateProcessor)
            .process()
    }
}