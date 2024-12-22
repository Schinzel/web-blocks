package io.schinzel.sample.pages.user_account.my_pe

import io.schinzel.page_elements.web_response.IPageElement

class MyPe(private val text: String) : IPageElement {
    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}