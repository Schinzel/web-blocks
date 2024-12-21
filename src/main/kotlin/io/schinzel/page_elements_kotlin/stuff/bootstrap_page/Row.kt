package io.schinzel.page_elements_kotlin.stuff.bootstrap_page

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