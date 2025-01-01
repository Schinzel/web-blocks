package io.schinzel.component.template_engine.file_reader

import java.io.File


/**
 * The purpose of this class is to read a file from the source code directory.
 * @param fileName The name of the file to read.
 * @param caller The class that is calling this method.
 */
class SourceFileReader(private val fileName: String, private val caller: Any) : IFileReader {

    override fun getFileContent(): String {
        // For example: Users/schinzel/code/page-elements-kotlin
        val projectDir = File("")
        // For example: io/schinzel/page_elements_kotlin/page/greeting_pe
        val pathToCallerClass = FileReaderUtil.getPathToCallerClass(caller)
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
}
