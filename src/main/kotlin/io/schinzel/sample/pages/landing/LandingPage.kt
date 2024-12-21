package io.schinzel.sample.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.pages.basic_page.BasicPage
import io.schinzel.page_elements.IPage
import io.schinzel.sample.pages.landing.greeting_pe.GreetingPe

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