package io.schinzel.web_blocks.component.template_engine.file_reader

/**
 * The purpose of this class is to create a file reader.
 *
 * The file reader is either a [JarFileReader] or a [SourceFileReader].
 * If the caller is running from a jar file, a [JarFileReader] is created, else
 * a [SourceFileReader] is created.
 */
object FileReaderFactory {
    /**
     * Creates a file reader.
     * @param caller The class that is calling this method.
     * @return A file reader.
     */
    fun create(caller: Any): IFileReader =
        when {
            isRunningFromJar(caller) -> JarFileReader(caller)
            else -> SourceFileReader(caller)
        }

    /**
     * @return True if the caller is running from a jar file.
     */
    private fun isRunningFromJar(caller: Any): Boolean {
        val location =
            caller::class.java.protectionDomain.codeSource.location
                .toString()
        return location.endsWith(".jar")
    }
}
