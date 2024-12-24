package io.schinzel.page_elements.file_util

class TemplateEngine(
    private val fileReader: IFileReader,
    private val templateProcessor: ITemplateProcessor
) {
    fun process(): String {
        val template = fileReader.getTemplate()
        return templateProcessor.processTemplate(template)
    }
}



