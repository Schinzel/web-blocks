package io.schinzel.page_elements_kotlin

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.stuff.TemplateProcessor

class BasicPage : IPage {
    private val pageElements = mutableListOf<IPageElement>()

    fun doIt(): String {
        return TemplateProcessor("BasicPage.html")
            .addData("firstName", "Pelle")
            .getFile()
    }

    override fun addPageElement(pageElement: IPageElement): BasicPage {
        pageElements.add(pageElement)
        return this
    }

    override fun getHtml(): String {
        return pageElements.joinToString(separator = "") { it.getHtml() }
    }
}

fun main() {
    BasicPage().doIt().println()
}