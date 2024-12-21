package io.schinzel.page_elements_kotlin.pages.settings.name

import io.schinzel.page_elements_kotlin.stuff.IPage

@Suppress("unused")
class Page: IPage {
    override fun getHtml(): String {
        return "<h1>Name</h1>"
    }
}