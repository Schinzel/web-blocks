package io.schinzel.page_elements_kotlin.page

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.BasicPage
import io.schinzel.page_elements_kotlin.page.greeting_pe.GreetingPe

class LandingPage {
    fun getHtml(): String {
        return BasicPage()
            .addPageElement(GreetingPe())
            .getHtml()
    }
}

fun main() {
    BasicPage()
        .addPageElement(GreetingPe())
        .getHtml()
        .println()
}