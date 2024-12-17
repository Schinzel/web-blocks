package io.schinzel.stuff

import io.schinzel.basic_utils_kotlin.printlnWithPrefix
import java.io.File


class TemplateProcessor(private val fileName: String, private val caller: Any) {
    val data: MutableMap<String, String> = mutableMapOf()

    fun addData(key: String, value: String): TemplateProcessor {
        data[key] = value
        return this
    }

    fun getProcessedTemplate(): String {
        val isJar = isRunningFromJar(caller)
        val fileContent = when {
            isJar -> readFileInJar(fileName, caller)
            else -> readFileInSrc(fileName, caller)
        }
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

        private fun isRunningFromJar(caller: Any): Boolean {
            val location = caller::class.java.protectionDomain.codeSource.location.toString()
            return location.endsWith(".jar")
        }

        private fun readFileInJar(fileName: String, caller: Any): String {
            // Get the package path of the caller
            val packagePath = caller::class.java.packageName.replace('.', File.separatorChar)
            val path = "/$packagePath/$fileName"
            return object {}.javaClass.getResource(path).readText()
        }


        private fun readFileInSrc(fileName: String, caller: Any): String {
            // For example: Users/schinzel/code/page-elements-kotlin
            val projectDir = File("")
            // For example: io/schinzel/page_elements_kotlin/page/greeting_pe
            val pathToCallerClass = caller::class.java.packageName.replace('.', File.separatorChar)
            // For example: /Users/schinzel/code/page-elements-kotlin/src/main/kotlin/io/schinzel/page_elements_kotlin/page/greeting_pe/GreetingPe.html
            val pathToFile = projectDir.absolutePath + "/src/main/kotlin/" + pathToCallerClass + "/" + fileName
            // Create file
            val file = File(pathToFile)
            return when {
                // If file exists, read it
                file.exists() -> file.readText()
                // If file does not exist, throw exception
                else -> throw Exception("File not found '$pathToFile'")
            }
        }
    }
}
