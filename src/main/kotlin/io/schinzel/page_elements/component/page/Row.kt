package io.schinzel.page_elements.component.page

import kotlinx.coroutines.*

/**
 * The purpose of this class is to represent a row in a web page.
 */
class Row {
    val columns: MutableList<Column> = mutableListOf()

    suspend fun getHtml(): String = supervisorScope {
        // Use supervisorScope for error isolation at column level
        // If one column fails, other columns continue rendering
        val columnsHtml = columns
            .map { async { it.getHtml() } }
            .awaitAll()
            .joinToString("\n")
        
        """
        |<div class="row">
        |  $columnsHtml
        |</div>
        """.trimMargin()
    }
}