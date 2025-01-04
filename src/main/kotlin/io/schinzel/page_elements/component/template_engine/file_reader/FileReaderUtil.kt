package io.schinzel.page_elements.component.template_engine.file_reader

import java.io.File

/**
 * The purpose of this class is to provide utility methods for file reading.
 */
object FileReaderUtil {
    /**
     * @return The path to the package of the caller class.
     */
    fun fromPackageToPath(caller: Any): String {
        return caller::class.java.packageName.replace('.', File.separatorChar)
    }
}
