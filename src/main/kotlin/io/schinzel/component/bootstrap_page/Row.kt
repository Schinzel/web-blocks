package io.schinzel.component.bootstrap_page

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