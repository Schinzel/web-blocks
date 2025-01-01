package io.schinzel.samples.component.pages.settings.address

import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class AddressWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}