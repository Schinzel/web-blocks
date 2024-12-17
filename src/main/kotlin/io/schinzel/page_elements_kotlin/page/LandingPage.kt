package io.schinzel.page_elements_kotlin.page

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.IPage
import io.schinzel.page_elements_kotlin.IPageElement
import io.schinzel.page_elements_kotlin.page.greeting_pe.GreetingPe

class LandingPage : IPage {
    private val pageElements = mutableListOf<IPageElement>()


    override fun addPageElement(pageElement: IPageElement): LandingPage {
        pageElements.add(pageElement)
        return this
    }

    override fun getHtml(): String {
        return pageElements.joinToString(separator = "") { it.getHtml() }
    }
}

fun main() {
    LandingPage()
        .addPageElement(GreetingPe())
        .getHtml()
        .println()
}