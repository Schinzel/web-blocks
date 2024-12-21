package io.schinzel.page_elements_kotlin.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.stuff.basic_page.BasicPage
import io.schinzel.page_elements_kotlin.pages.landing.greeting_pe.GreetingPe
import io.schinzel.page_elements_kotlin.stuff.IPage
import io.schinzel.page_elements_kotlin.stuff.find_pages.annotations.Page

@Page
class LandingPage(userId: String = ""): IPage {
    init {
        "LandingPage created with userId: $userId".println()
    }

    override fun getResponse(): String {
        return BasicPage()
            .setTitle("Welcome")
            .addPageElement(GreetingPe())
            .getResponse()
    }
}

fun main() {
    BasicPage()
        .addPageElement(GreetingPe())
        .getResponse()
        .println()
}