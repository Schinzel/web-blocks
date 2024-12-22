package io.schinzel.pages.basic_page

import io.schinzel.page_elements.IWebPage
import io.schinzel.page_elements.IPageElement
import io.schinzel.page_elements.TemplateProcessor

class BasicWebPage : IWebPage {
    private val pageElements = mutableListOf<IPageElement>()
    private var title = ""

    fun setTitle(title: String): BasicWebPage {
        this.title = title
        return this
    }

    fun addPageElement(pageElement: IPageElement): IWebPage {
        pageElements.add(pageElement)
        return this
    }

    override fun getResponse(): String {
        val content = pageElements.joinToString(separator = "") { it.getHtml() }
        return TemplateProcessor("template.html", this)
            .addData("title", title)
            .addData("content", content)
            .getProcessedTemplate()
    }
}