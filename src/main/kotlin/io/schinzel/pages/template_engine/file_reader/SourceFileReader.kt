package io.schinzel.pages.template_engine.file_reader

import java.io.File

interface IFileReader {
    fun getFileContent(): String
}

object FileReaderFactory {
    fun create(fileName: String, caller: Any): IFileReader = when {
        isRunningFromJar() -> JarFileReader(fileName, caller)
        else -> SourceFileReader(fileName, caller)
    }

    /**
     * @return True if the caller is running from a jar file.
     */
    private fun isRunningFromJar(): Boolean {
        val location = this::class.java.protectionDomain.codeSource.location.toString()
        return location.endsWith(".jar")
    }
}


object FileReaderUtil {
    /**
     * @return The path to the package of the caller class.
     * For example: io/schinzel/page_elements_kotlin/page/greeting_pe
     */
    fun getPathToCallerClass(caller: Any): String {
        return caller::class.java.packageName.replace('.', File.separatorChar)
    }
}


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