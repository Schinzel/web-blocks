package io.schinzel.web_blocks.web.routes_overview

import org.jsoup.nodes.Entities

/**
 * Utility for HTML escaping to prevent XSS vulnerabilities.
 * Provides safe HTML escaping for user-controlled data.
 *
 * Written by Claude Sonnet 4
 */
object HtmlEscapeUtil {
    /**
     * Escapes HTML characters to prevent XSS vulnerabilities
     * @param text The text to escape, can be null
     * @return HTML-escaped text or empty string if input was null
     */
    fun escapeHtml(text: String?): String = if (text != null) Entities.escape(text) else ""

    /**
     * Converts a KClass to its relative file path in the project
     * Example: io.schinzel.sample.api.UserPets -> src/main/kotlin/io/schinzel/sample/api/UserPets.kt
     */
    fun getRelativeFilePath(kClass: kotlin.reflect.KClass<*>): String {
        val packagePath = kClass.java.packageName.replace('.', '/')
        val className = kClass.simpleName ?: "Unknown"
        return "src/main/kotlin/$packagePath/$className.kt"
    }
}
