package io.schinzel.pages.basic_page

import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler
import io.schinzel.sample2.pages.user_account.name_pe.IPageElement
import io.schinzel.pages.template_engine.TemplateRenderer

class BasicWebPage : IPageResponseHandler {
    private val pageElements = mutableListOf<IPageElement>()
    private var title = ""

    fun setTitle(title: String): BasicWebPage {
        this.title = title
        return this
    }

    fun addPageElement(pageElement: IPageElement): IPageResponseHandler {
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