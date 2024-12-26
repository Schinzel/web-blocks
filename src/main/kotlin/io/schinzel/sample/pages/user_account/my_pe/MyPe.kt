package io.schinzel.sample.pages.user_account.my_pe

import io.schinzel.pages.IPageElement

class MyPe(private val text: String) : IPageElement {

    override fun getHtml(): String {
        return "<div>$text</div>"
    }
}

class MyClass(private val text1: String, private val text2: String) : IPageElement {
    private val textA = ""

    private val textB = ""

    override fun getHtml(): String {
        return "<div>text</div>"
    }
}