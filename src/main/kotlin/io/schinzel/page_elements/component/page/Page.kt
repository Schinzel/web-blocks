package io.schinzel.page_elements.component.page

import io.schinzel.page_elements.component.template_engine.TemplateProcessor

/**
 * The purpose of this class is to represent a web page.
 *
 * A page consists of a set of rows. Each row consists of a set of columns.
 * Each column consists of a set of page elements.
 */
class Page {
    // The title of the page
    private var title = ""

    // The rows of the page
    private val rows = mutableListOf<Row>()

    /**
     * Sets the title of the page.
     * @param title The title of the page
     * @return This page
     */
    fun setTitle(title: String): Page {
        this.title = title
        return this
    }

    /**
     * Adds a row to the page.
     * @return This page
     */
    fun addRow(): Page {
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
    fun addColumn(columnSpan: Int): Page {
        rows.last().columns.add(Column(columnSpan))
        return this
    }

    /**
     * Adds a page element to the last column of the last row.
     * @param pageElement The page element to add
     * @return This page
     */
    fun addPageElement(pageElement: ObservablePageElement): Page {
        if (rows.isEmpty() || rows.last().columns.isEmpty()) {
            throw Exception("You must add a row and a column before adding a page element")
        }
        rows.last().columns.last().elements.add(pageElement)
        return this
    }

    fun getHtml(): String {
        // The content of the page
        val content = rows.joinToString("\n") { it.getHtml() }
        // Insert the title and content into the page template
        return TemplateProcessor(this)
            .addData("title", title)
            .addData("content", content)
            .processTemplate("html/page-template.html")
    }
}