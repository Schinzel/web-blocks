package io.schinzel.pages.template_engine.file_reader

/**
 * The purpose of this interface is to provide a function for reading a file.
 */
interface IFileReader {
    fun getFileContent(): String
}
