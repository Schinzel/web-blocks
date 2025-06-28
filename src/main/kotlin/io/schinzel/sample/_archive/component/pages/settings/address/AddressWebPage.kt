package io.schinzel.sample._archive.component.pages.settings.address

import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class AddressWebPage: IPageRoute {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}