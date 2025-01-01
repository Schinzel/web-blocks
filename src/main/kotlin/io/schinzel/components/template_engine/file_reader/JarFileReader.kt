package io.schinzel.components.template_engine.file_reader


/**
 * The purpose of this class is to read a file from a jar file.
 * @param fileName The name of the file to read.
 * @param caller The class that is calling this class.
 */
class JarFileReader(private val fileName: String, private val caller: Any) : IFileReader {

    override fun getFileContent(): String {
        // For example: io/schinzel/page_elements_kotlin/page/greeting_pe
        val pathToCallerClass = FileReaderUtil.getPathToCallerClass(caller)
        // For example: /io/schinzel/page_elements_kotlin/page/greeting_pe/GreetingPe.html
        val pathToFile = "/$pathToCallerClass/$fileName"
        return object {}.javaClass.getResource(pathToFile)?.readText()
            ?: throw Exception("File not found '$pathToFile'")
    }
}
