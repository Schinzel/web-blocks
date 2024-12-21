package io.schinzel.page_elements_kotlin.stuff.bootstrap_page

import io.schinzel.page_elements_kotlin.stuff.IPageElement

class Column(val span: Int) {
    val elements: MutableList<IPageElement> = mutableListOf()

    // |<div class="col-$span">
    fun getHtml(): String {
        return """
            |<div class="col-$span">
            |  ${elements.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}
