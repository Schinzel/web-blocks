package io.schinzel.sample.pages.sub_dir.sub_dir_2

import io.schinzel.page_elements.IPage

@Suppress("unused")
class MyPage: IPage {
    override fun getHtml(): String {
        return "<h1>Sub dir page</h1>"
    }
}