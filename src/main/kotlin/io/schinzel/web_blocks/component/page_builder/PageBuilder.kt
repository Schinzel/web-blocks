package io.schinzel.web_blocks.component.page_builder

import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope

/**
 * The purpose of this class is to represent a web page.
 *
 * A page consists of a set of rows. Each row consists of a set of columns.
 * Each column consists of a set of blocks.
 */
class PageBuilder {
    // The title of the page
    private var title = ""

    // The rows of the page
    private val rows = mutableListOf<Row>()

    /**
     * Sets the title of the page.
     * @param title The title of the page
     * @return This page
     */
    fun setTitle(title: String): PageBuilder {
        this.title = title
        return this
    }

    /**
     * Adds a row to the page.
     * @return This page
     */
    fun addRow(): PageBuilder {
        rows.add(Row())
        return this
    }

    /**
     * Adds a column to the last row.
     *
     * @param columnSpan The number of Bootstrap grid columns (out of 12) that this column should span.
     * The columns on a row should have a combined column span of 12.
     * @return This page
     */
    fun addColumn(columnSpan: Int): PageBuilder {
        rows.last().columns.add(Column(columnSpan))
        return this
    }

    /**
     * Adds a block to the last column of the last row.
     * @param block The block to add
     * @return This page
     */
    fun addBlock(block: IBlock): PageBuilder {
        if (rows.isEmpty() || rows.last().columns.isEmpty()) {
            throw Exception("You must add a row and a column before adding a block")
        }
        rows
            .last()
            .columns
            .last()
            .blocks
            .add(block)
        return this
    }

    suspend fun getHtml(): String =
        supervisorScope {
            // Use supervisorScope for error isolation at row level
            // If one row fails, other rows continue rendering
            val rowsHtml =
                rows
                    .map { async { it.getHtml() } }
                    .awaitAll()
                    .joinToString("\n")

            return@supervisorScope TemplateProcessor(this@PageBuilder)
                .withData("title", title)
                .withData("content", rowsHtml)
                .processTemplate("html/page-template.html")
        }
}
