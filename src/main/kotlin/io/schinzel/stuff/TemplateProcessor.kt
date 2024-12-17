package io.schinzel.stuff

import io.schinzel.basic_utils_kotlin.println
import org.apache.commons.io.FileUtils
import java.io.File

class TemplateProcessor(private val fileName: String) {
    private val data = mutableMapOf<String, String>()

    fun addData(key: String, value: String): TemplateProcessor {
        data[key] = value
        return this
    }

    fun getFile(): String {
        val fileContent = readFile(fileName)
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

        private fun readFile(fileName: String): String {
            // Current directory
            val directory = File(".")
            // Get all HTML files in the current directory
            val htmlFiles = FileUtils.listFiles(directory, arrayOf("html"), true)
            // Loop through all HTML files
            htmlFiles.forEach { file ->
                // If the file name matches the file name we are looking for
                if (file.name == fileName) {
                    println("Found HTML file: ${file.absolutePath}")
                    // Return the content of the file
                    return file.readText()
                }
            }
            throw Exception("File not found")
        }
    }
}


fun main() {
    TemplateProcessor("BasicPage.html")
        .addData("firstName", "Pelle")
        .getFile()
        .println()
}