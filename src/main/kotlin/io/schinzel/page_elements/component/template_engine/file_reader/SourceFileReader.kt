package io.schinzel.page_elements.component.template_engine.file_reader

import java.io.File

/**
 * The purpose of this class is to read a file from the source code directory.
 * @param caller The class that is calling this class.
 */
class SourceFileReader(private val caller: Any) : IFileReader {
    private val absolutePathToCaller: String

    init {
        // For example: io/schinzel/page_elements/samples/component/pages
        val pathFromProjectRootToCaller = FileReaderUtil.pathFromProjectRootToCaller(caller)
        // For example: Users/schinzel/code/page-elements-kotlin
        val absolutePathToProjectRoot = File("").absolutePath
        // For example: /Users/schinzel/code/page-elements-kotlin/io/schinzel/page_elements/samples/component/pages
        absolutePathToCaller = "$absolutePathToProjectRoot/src/main/kotlin/$pathFromProjectRootToCaller"


    }

    /**
     * Reads a file from the source code directory.
     * @param filePath The path and name of the file to read relative the the caller.
     * For example: "template.html" or "html/page-template.html"
     * @return The content of the file.
     */
    override fun getFileContent(filePath: String): String {
        // Create file
        val file = getFile(filePath)
        return when {
            // If file exists, read it
            file.exists() -> file.readText()
            // If file does not exist, throw exception
            else -> throw Exception("File not found '${getAbsolutePathToFile(filePath)}'")
        }
    }

    override fun fileExists(filePath: String): Boolean = getFile(filePath).exists()

    private fun getFile(filePath: String): File {
        val pathToFile = getAbsolutePathToFile(filePath)
        return File(pathToFile)
    }


    private fun getAbsolutePathToFile(filePath: String): String = "$absolutePathToCaller/$filePath"
}