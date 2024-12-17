package io.schinzel.page_elements_kotlin


interface IPage {
    fun addPageElement(pageElement: IPageElement): IPage
    fun getHtml(): String
}

interface IPageElement {
    fun getHtml(): String
}



class BasicPage : IPage {
    private val pageElements = mutableListOf<IPageElement>()


    override fun addPageElement(pageElement: IPageElement): IPage {
        pageElements.add(pageElement)
        return this
    }

    override fun getHtml(): String {
        return pageElements.joinToString(separator = "") { it.getHtml() }
    }
}