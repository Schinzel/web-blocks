package io.schinzel.page_elements.component.template_engine.file_reader

import java.io.File

/**
 * The purpose of this class is to read a file from the source code directory.
 * @param caller The class that is calling this class.
 * For example: io/schinzel/samples/component/pages/user_account/intro_text
 */
class SourceFileReader(private val caller: Any) : IFileReader {


    override fun getFileContent(fileName: String): String {
        // For example: io/schinzel/page_elements/samples/component/pages
        val pathFromProjectRootToCaller = FileReaderUtil
            .pathFromProjectRootToCaller(caller)
        // For example: Users/schinzel/code/page-elements-kotlin
        val pathToProjectRoot = File("").absolutePath
        // For example: /Users/schinzel/code/page-elements-kotlin/
        // src/main/kotlin/io/schinzel/page_elements_kotlin/page/greeting_pe/GreetingPe.html
        val pathToFile = "$pathToProjectRoot/src/main/kotlin/$pathFromProjectRootToCaller/$fileName"
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