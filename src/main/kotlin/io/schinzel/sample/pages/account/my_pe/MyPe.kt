package io.schinzel.sample.pages.account.my_pe

import io.schinzel.page_elements.stuff.IPageElement

class MyPe(private val text: String) : IPageElement {
    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}