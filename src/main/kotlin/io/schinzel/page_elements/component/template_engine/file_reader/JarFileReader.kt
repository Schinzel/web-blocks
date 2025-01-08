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

    override fun getFileContent(fileName: String): String {
        // For example: io/schinzel/page_elements/samples/component/pages
        val pathFromProjectRootToCaller = FileReaderUtil.pathFromProjectRootToCaller(caller)
        // Get the full path to the file
        // For example: /io/schinzel/samples/component/pages/user_account/intro_text/template.html
        val pathToFile = "/$pathFromProjectRootToCaller/$fileName"
        // Return cached content if exists, otherwise read file and cache it
        return cache.getOrPut(pathToFile) {
            // Read the file
            object {}.javaClass.getResource(pathToFile)?.readText()
                ?: throw Exception("File not found '$pathToFile'")
        }
    }
}
