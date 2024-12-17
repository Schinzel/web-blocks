package io.schinzel.page_elements_kotlin.account.my_pe

import io.schinzel.stuff.IPageElement

class MyPe(private val text: String) : IPageElement {
    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}