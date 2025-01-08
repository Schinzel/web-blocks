package io.schinzel.page_elements.component.template_engine.file_reader


/**
 * The purpose of this class is to read a file from a jar file.
 * Files are cached in memory.
 * @param caller The class that is calling this class.
 */
class JarFileReader(private val caller: Any) : IFileReader {

    companion object {
        private val cache = mutableMapOf<String, String>()
    }

    /**
     * Reads a file from the jar file.
     * @param filePath The path and name of the file to read relative the the caller.
     * For example: "template.html" or "html/page-template.html"
     * @return The content of the file.
     */
    override fun getFileContent(filePath: String): String {
        // For example: io/schinzel/page_elements/samples/component/pages
        val pathFromProjectRootToCaller = FileReaderUtil.pathFromProjectRootToCaller(caller)
        // Get the full path to the file
        // For example: /io/schinzel/samples/component/pages/user_account/intro_text/template.html
        val pathToFile = "/$pathFromProjectRootToCaller/$filePath"
        // Return cached content if exists, otherwise read file and cache it
        return cache.getOrPut(pathToFile) {
            // Read the file
            object {}.javaClass.getResource(pathToFile)?.readText()
                ?: throw Exception("File not found '$pathToFile'")
        }
    }
}
