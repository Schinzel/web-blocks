package io.schinzel.pages.template_engine

import java.io.File

/**
 * The purpose of this interface is to read a file.
 */
interface IFileReader {
    fun getFileContent(): String
}


/**
 * @param fileName The name of the file to process.
 * @param caller The class that is calling this class. Used to find the file to read.
 */
class FileFileReader(private val fileName: String, private val caller: Any) : IFileReader {

    override fun getFileContent(): String {
        return when {
            isRunningFromJar() -> readFileInJar(fileName, caller)
            else -> readFileInSrc(fileName, caller)
        }
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
