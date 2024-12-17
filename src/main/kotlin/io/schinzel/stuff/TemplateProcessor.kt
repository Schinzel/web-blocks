package io.schinzel.stuff

import java.io.File


/**
 * The purpose of this class is to process a template file and replace placeholders with values.
 * The file to be read must be located in the same package as the class that is calling this class.
 *
 * @param fileName The name of the file to process.
 * @param caller The class that is calling this class. Used to find the file to read.
 */
class TemplateProcessor(private val fileName: String, private val caller: Any) {
    val data: MutableMap<String, String> = mutableMapOf()

    fun addData(key: String, value: String): TemplateProcessor {
        data[key] = value
        return this
    }

    fun getProcessedTemplate(): String {
        val fileContent = when {
            isRunningFromJar() -> readFileInJar(fileName, caller)
            else -> readFileInSrc(fileName, caller)
        }
        return applyData(fileContent, data)
    }


    companion object {

        /**
         * @param fileContent The content of the file to process.
         * @param data The data to replace placeholders with.
         * @return The file content with placeholders replaced with values from the data map.
         */
        private fun applyData(fileContent: String, data: Map<String, String>): String {
            var content = fileContent
            data.forEach { (key, value) ->
                content = content.replace("{{$key}}", value)
            }
            return content
        }

        /**
         * Reads a file from a jar file.
         *
         * @param fileName The name of the file to read.
         * @param caller The class that is calling this class. Used to find the file to read.
         * @return The content of the file.
         */
        private fun readFileInJar(fileName: String, caller: Any): String {
            // For example: io/schinzel/page_elements_kotlin/page/greeting_pe
            val pathToCallerClass = getPathToCallerClass(caller)
            // For example: /io/schinzel/page_elements_kotlin/page/greeting_pe/GreetingPe.html
            val pathToFile = "/$pathToCallerClass/$fileName"
            return object {}.javaClass.getResource(pathToFile)?.readText()
                ?: throw Exception("File not found '$pathToFile'")
        }


        /**
         *  Reads a file from the src folder.
         *
         * @param fileName The name of the file to read.
         * @param caller The class that is calling this class. Used to find the file to read.
         * @return The content of the file.
         */
        private fun readFileInSrc(fileName: String, caller: Any): String {
            // For example: Users/schinzel/code/page-elements-kotlin
            val projectDir = File("")
            // For example: io/schinzel/page_elements_kotlin/page/greeting_pe
            val pathToCallerClass = getPathToCallerClass(caller)
            // For example: /Users/schinzel/code/page-elements-kotlin/
            // src/main/kotlin/io/schinzel/page_elements_kotlin/page/greeting_pe/GreetingPe.html
            val pathToFile = projectDir.absolutePath + "/src/main/kotlin/" +
                    pathToCallerClass + "/" + fileName
            // Create file
            val file = File(pathToFile)
            return when {
                // If file exists, read it
                file.exists() -> file.readText()
                // If file does not exist, throw exception
                else -> throw Exception("File not found '$pathToFile'")
            }
        }

        /**
         * @return The path to the package of the caller class.
         * For example: io/schinzel/page_elements_kotlin/page/greeting_pe
         */
        private fun getPathToCallerClass(caller: Any): String {
            return caller::class.java.packageName.replace('.', File.separatorChar)
        }


        /**
         * @return True if the caller is running from a jar file.
         */
        private fun isRunningFromJar(): Boolean {
            val location = this::class.java.protectionDomain.codeSource.location.toString()
            return location.endsWith(".jar")
        }
    }
}
