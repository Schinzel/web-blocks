package io.schinzel.page_elements.stuff.basic_page

import io.schinzel.page_elements.stuff.IPage
import io.schinzel.page_elements.stuff.IPageElement
import io.schinzel.page_elements.stuff.TemplateProcessor

class BasicPage : IPage {
    private val pageElements = mutableListOf<IPageElement>()
    private var title = ""

    fun setTitle(title: String): BasicPage {
        this.title = title
        return this
    }

    fun addPageElement(pageElement: IPageElement): IPage {
        pageElements.add(pageElement)
        return this
    }

    override fun getHtml(): String {
        val content = pageElements.joinToString(separator = "") { it.getHtml() }
        return TemplateProcessor("template.html", this)
            .addData("title", title)
            .addData("content", content)
            .getProcessedTemplate()
    }
}