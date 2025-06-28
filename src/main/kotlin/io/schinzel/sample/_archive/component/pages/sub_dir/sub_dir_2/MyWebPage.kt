package io.schinzel.sample._archive.component.pages.sub_dir.sub_dir_2

import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class MyWebPage: IPageRoute {
    override fun getResponse(): String {
        return "<h1>Sub dir page</h1>"
    }
}