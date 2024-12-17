package io.schinzel.stuff

interface IPage {
    fun addPageElement(pageElement: IPageElement): IPage
    fun getHtml(): String
}