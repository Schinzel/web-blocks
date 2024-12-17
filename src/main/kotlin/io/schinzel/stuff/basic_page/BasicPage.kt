package io.schinzel.stuff.basic_page

import io.schinzel.stuff.IPage
import io.schinzel.stuff.IPageElement
import io.schinzel.stuff.TemplateProcessor

class BasicPage : IPage {
    private val pageElements = mutableListOf<IPageElement>()
    private var title = ""

    fun setTitle(title: String): BasicPage {
        this.title = title
        return this
    }

    override fun addPageElement(pageElement: IPageElement): IPage {
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