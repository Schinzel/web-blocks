package io.schinzel.pages.template_engine

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
class TemplateProcessor(private val caller: Any): ITemplateProcessor {
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

        fun processIncludeFiles(templateFileContent: String, caller: Any): String {
            var processedTemplate = templateFileContent
            while (true) {
                // Find next include
                val startIndex = processedTemplate.indexOf(INCLUDE_FILE_START)
                if (startIndex == -1) break

                // Find end of include
                val endIndex = processedTemplate.indexOf(INCLUDE_FILE_END, startIndex)
                if (endIndex == -1) break

                // Extract filename
                val fileName = processedTemplate
                    .substring(startIndex + INCLUDE_FILE_START.length, endIndex)
                    .trim()

                // Replace include with file content
                val fileContent = FileReader(fileName, caller).getFileContent()
                processedTemplate = processedTemplate.substring(0, startIndex) +
                        fileContent +
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
}