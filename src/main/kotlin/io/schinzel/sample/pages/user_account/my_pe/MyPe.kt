package io.schinzel.sample.pages.user_account.my_pe

import io.schinzel.sample2.pages.user_account.name_pe.IPageElement

class MyPe(private val text: String) : IPageElement {

    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}
