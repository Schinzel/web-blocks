package io.schinzel.page_elements_kotlin


interface IPage {
    fun addPageElement(pageElement: IPageElement): IPage
    fun getHtml(): String
}

interface IPageElement {
    fun getHtml(): String
}

