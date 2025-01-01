package io.schinzel.component.template_engine.file_reader

/**
 * The purpose of this class is to create a file reader.
 */
object FileReaderFactory {

    /**
     * Creates a file reader.
     * @param fileName The name of the file to read.
     * @param caller The class that is calling this method.
     * @return A file reader.
     */
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
