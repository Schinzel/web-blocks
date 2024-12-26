package io.schinzel.sample.pages.settings.address

import io.schinzel.web_app_engine.route_registry.processors.IPageResponseHandler

@Suppress("unused")
class AddressWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Address</h1>"
    }
}