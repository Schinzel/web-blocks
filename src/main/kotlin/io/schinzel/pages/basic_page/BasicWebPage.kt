package io.schinzel.pages.basic_page

import io.schinzel.web_app_engine.route_registry.processors.IWebPage
import io.schinzel.pages.IPageElement
import io.schinzel.web_app_engine.template_engine.TemplateRenderer

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
        return TemplateRenderer("template.html", this)
            .addData("title", title)
            .addData("content", content)
            .process()
    }
}