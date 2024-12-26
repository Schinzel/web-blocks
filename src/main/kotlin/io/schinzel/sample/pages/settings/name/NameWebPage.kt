package io.schinzel.sample.pages.settings.name

import io.schinzel.web_app_engine.route_registry.processors.IPageResponseHandler

@Suppress("unused")
class NameWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}