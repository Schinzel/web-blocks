package io.schinzel.component.bootstrap_page

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