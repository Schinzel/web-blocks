package io.schinzel._old_samples.component.pages.settings.name

import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class NameWebPage: IPageRoute {
    override fun getResponse(): String {
        return "<h1>Name</h1>"
    }
}