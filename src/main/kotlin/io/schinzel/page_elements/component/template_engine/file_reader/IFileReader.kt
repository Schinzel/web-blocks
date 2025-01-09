package io.schinzel.page_elements.component.template_engine.file_reader

import java.io.File

/**
 * The purpose of this interface is to provide a function for reading a file.
 */
interface IFileReader {
    val pathToCaller: String

    fun getFileContent(filePath: String): String

    fun getAbsolutePathToFile(filePath: String): String = "$pathToCaller/$filePath"

    fun getFile(filePath: String): File {
        val pathToFile = getAbsolutePathToFile(filePath)
        return File(pathToFile)
    }
}
