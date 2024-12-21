package io.schinzel.page_elements_kotlin.pages.account.my_pe

import io.schinzel.page_elements_kotlin.stuff.IPageElement

class MyPe(private val text: String) : IPageElement {
    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}