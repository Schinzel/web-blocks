package io.schinzel.component.page

/**
 * The purpose of this class is to represent a row in a web page.
 */
class Row {
    val columns: MutableList<Column> = mutableListOf()

    fun getHtml(): String {
        return """
            |<div class="row">
            |  ${columns.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}