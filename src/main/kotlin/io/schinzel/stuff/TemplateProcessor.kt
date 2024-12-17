package io.schinzel.stuff

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import org.apache.commons.io.FileUtils
import java.io.File


class TemplateProcessor(private val fileName: String, private val caller: Any) {
    val data: MutableMap<String, String> = mutableMapOf()

    fun addData(key: String, value: String): TemplateProcessor {
        data[key] = value
        return this
    }

    fun getProcessedTemplate(): String {
        val fileContent = readFile(fileName, caller)
        return applyData(fileContent, data)
    }

    companion object {

        private fun applyData(fileContent: String, data: Map<String, String>): String {
            var content = fileContent
            data.forEach { (key, value) ->
                content = content.replace("{{$key}}", value)
            }
            return content
        }

        private fun readFile(fileName: String, caller: Any): String {
            // Get the package path of the caller
            val packagePath = caller::class.java.packageName.replace('.', File.separatorChar)

            val baseDirectory = File("")
            val newPath = baseDirectory.absolutePath + "/src/main/kotlin/" + packagePath
            val newPathFile = File (newPath)

            // Get all HTML files in all directories below the current directory
            val htmlFiles = FileUtils.listFiles(newPathFile, arrayOf("html"), true)
            // Loop through all HTML files
            htmlFiles.forEach { file ->
                // If the file name matches the file name we are looking for
                if (file.name == fileName) {
                    file.absolutePath.printlnWithPrefix("File abs path")
                    // Return the content of the file
                    return file.readText()
                }
            }
            throw Exception("File not found '$fileName'")
        }
    }
}
