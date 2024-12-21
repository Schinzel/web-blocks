package io.schinzel.sample.pages.settings.name

import io.schinzel.page_elements.stuff.IPage

@Suppress("unused")
class Page: IPage {
    override fun getHtml(): String {
        return "<h1>Name</h1>"
    }
}