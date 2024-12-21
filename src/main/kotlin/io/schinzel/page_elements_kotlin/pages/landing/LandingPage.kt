package io.schinzel.page_elements_kotlin.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.basic_page.BasicPage
import io.schinzel.page_elements_kotlin.pages.landing.greeting_pe.GreetingPe
import io.schinzel.page_elements_kotlin.stuff.IPage

class LandingPage(userId: String = ""): IPage {
    init {
        "LandingPage created with userId: $userId".println()
    }

    override fun getHtml(): String {
        return BasicPage()
            .setTitle("Welcome")
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