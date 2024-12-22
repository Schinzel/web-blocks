package io.schinzel.sample.pages.settings.address

import io.schinzel.page_elements.IWebPage

@Suppress("unused")
class AddressWebPage: IWebPage {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}