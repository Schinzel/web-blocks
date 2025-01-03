package io.schinzel.page_elements.component.page

/**
 * The purpose of this class is to represent a column in a web page.
 */
class Column(val span: Int) {
    val elements: MutableList<ObservablePageElement> = mutableListOf()

    fun getHtml(): String {
        return """
            |<div class="col-$span">
            |  ${elements.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}