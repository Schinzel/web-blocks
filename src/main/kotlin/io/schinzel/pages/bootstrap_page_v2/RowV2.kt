package io.schinzel.pages.bootstrap_page_v2

class RowV2 {
    val columns: MutableList<ColumnV2> = mutableListOf()

    fun getHtml(): String {
        return """
            |<div class="row">
            |  ${columns.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}