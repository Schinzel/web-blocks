package io.schinzel.sample.pages.settings.address

import io.schinzel.web_app_engine.IWebPage

@Suppress("unused")
class AddressWebPage: IWebPage {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}