package io.schinzel.component.page

import io.schinzel.component.template_engine.TemplateRenderer

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
     * @param span The span of the column
     * @return This page
     */
    fun addColumn(span: Int): Page {
        rows.last().columns.add(Column(span))
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
        return TemplateRenderer("html/page-template.html", this)
            .addData("title", title)
            .addData("content", content)
            .process()
    }
}