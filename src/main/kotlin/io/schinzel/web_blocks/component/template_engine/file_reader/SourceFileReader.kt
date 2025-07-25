package io.schinzel.web_blocks.component.template_engine.file_reader

import java.io.File

/**
 * The purpose of this class is to read a file from the source code directory.
 * @param caller The class that is calling this class.
 */
class SourceFileReader(
    private val caller: Any,
) : IFileReader {
    override val pathToCaller: String

    init {
        // Single module example: /Users/schinzel/code/web-blocks/target/classes/
        // Multi module example: /Users/schinzel/code/wms2/modules/sites/admin/target/classes/
        val classPath: String =
            caller::class.java.protectionDomain.codeSource.location
                .toURI()
                .path

        // Single module example: io/schinzel/web_blocks/component/page_builder
        // Multi module example: io/schinzel/sites/admin/pages/page_with_blocks
        val packagePath: String = caller::class.java.packageName.replace('.', '/')

        // Find where "src/main/kotlin" appears in the filesystem
        val targetDir = File(classPath)
        var currentDir: File = targetDir

        // Walk up until we find src/main/kotlin or reach root
        while (currentDir.parentFile != null && !File(currentDir, "src/main/kotlin").exists()) {
            currentDir = currentDir.parentFile
        }

        pathToCaller =
            when {
                // Case: Found src/main/kotlin in directory tree (works for both single and multi-module projects)
                File(currentDir, "src/main/kotlin").exists() -> {
                    // Single module example: /Users/schinzel/code/web-blocks/src/main/kotlin/io/schinzel/web_blocks/component/page_builder
                    // Multi module example: /Users/schinzel/code/wms2/modules/sites/admin/src/main/kotlin/io/schinzel/sites/admin/pages/page_with_blocks
                    "${currentDir.absolutePath}/src/main/kotlin/$packagePath"
                }
                // Case: Could not find src/main/kotlin anywhere in the directory tree
                else -> {
                    // This means non-standard project structure - fail fast with clear error
                    throw IllegalStateException("Could not find 'src/main/kotlin' directory in project structure")
                }
            }
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
}
