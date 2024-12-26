package io.schinzel.sample.pages.sub_dir.sub_dir_2

import io.schinzel.web_app_engine.route_registry.processors.IPageResponseHandler

@Suppress("unused")
class MyWebPage: IPageResponseHandler {
    override fun getResponse(): String {
        return "<h1>Sub dir page</h1>"
    }
}