package io.schinzel.pages.bootstrap_page

import io.schinzel.sample2.pages.user_account.name_pe.IPageElement

class Column(val span: Int) {
    val elements: MutableList<IPageElement> = mutableListOf()

    fun getHtml(): String {
        return """
            |<div class="col-$span">
            |  ${elements.joinToString("\n") { it.getHtml() }}
            |</div>
        """.trimMargin()
    }
}
