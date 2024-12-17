package io.schinzel.stuff

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import io.schinzel.page_elements_kotlin.IPageElement
import org.apache.commons.io.FileUtils
import java.io.File


interface IFileTemplatePageElement : IPageElement, ITemplateProcessor


interface ITemplateProcessor {
    fun addData(key: String, value: String): ITemplateProcessor
    fun getFile(): String
}

interface ITemplateProcessorDeep  {
    val data: MutableMap<String, String>

    fun addData(key: String, value: String): ITemplateProcessorDeep {
        data[key] = value
        return this
    }

    fun getFile(fileName: String): String {
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
            directory.absolutePath.printlnWithPrefix("Current directory")
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
            throw Exception("File not found '$fileName'")
        }
    }
}

class TemplateProcessorDeep  {
    val data: MutableMap<String, String> = mutableMapOf()

    fun addData(key: String, value: String): TemplateProcessorDeep {
        data[key] = value
        return this
    }

    fun getFile(fileName: String): String {
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
            directory.absolutePath.printlnWithPrefix("Current directory")
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
            throw Exception("File not found '$fileName'")
        }
    }
}



fun main() {
}