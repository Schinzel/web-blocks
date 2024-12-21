package io.schinzel.page_elements_kotlin.pages.settings.address

import io.schinzel.page_elements_kotlin.stuff.IPage

@Suppress("unused")
class Page: IPage {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}