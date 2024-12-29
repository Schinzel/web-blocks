package io.schinzel.pages.bootstrap_page_v2

class ColumnV2(val span: Int) {
    val elements: MutableList<ObservablePageElement> = mutableListOf()

    fun getHtml(): String {
        return """
            |<div class="col-$span">
            |  ${elements.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}