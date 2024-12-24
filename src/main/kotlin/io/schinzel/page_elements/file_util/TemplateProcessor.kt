package io.schinzel.page_elements.file_util

interface ITemplateProcessor {
    fun addData(key: String, value: String): ITemplateProcessor
    fun processTemplate(template: String): String
}

class TemplateProcessor: ITemplateProcessor {
    val data: MutableMap<String, String> = mutableMapOf()

    override fun addData(key: String, value: String): ITemplateProcessor {
        data[key] = value
        return this
    }

    override fun processTemplate(template: String): String {
        return applyData(template, data)
    }


    companion object {
        /**
         * @param fileContent The content of the file to process.
         * @param data The data to replace placeholders with.
         * @return The file content with placeholders replaced with values from the data map.
         */
        private fun applyData(fileContent: String, data: Map<String, String>): String {
            return data.entries.fold(fileContent) { content, (key, value) ->
                content.replace("{{$key}}", value)
            }
        }
    }
}