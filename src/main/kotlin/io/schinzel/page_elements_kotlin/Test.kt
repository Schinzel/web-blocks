package io.schinzel.page_elements_kotlin

import io.schinzel.basic_utils_kotlin.println

class Test {
}

interface IPage{
    fun addPageElement(pageElement: IPageElement): IPage
    fun getHtml(): String
}

interface IPageElement {
    fun getHtml(): String
}

interface ITabbedPage {
    fun addTab(tab: ITab): ITabbedPage
    fun getHtml(): String
}

interface ITab {
    fun addPageElement(pageElement: IPageElement): ITab
    fun getHtml(): String
}

class HeaderPageElement : IPageElement {
    override fun getHtml(): String {
        return "<header>Header</header>"
    }
}

fun main() {
    BasicPage()
}