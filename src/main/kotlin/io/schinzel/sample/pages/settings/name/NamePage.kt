package io.schinzel.sample.pages.settings.name

import io.schinzel.page_elements.IPage

@Suppress("unused")
class NamePage: IPage {
    override fun getHtml(): String {
        return "<h1>Name</h1>"
    }
}