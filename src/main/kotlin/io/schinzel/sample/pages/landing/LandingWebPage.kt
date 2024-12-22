package io.schinzel.sample.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.pages.basic_page.BasicWebPage
import io.schinzel.page_elements.IWebPage
import io.schinzel.sample.pages.landing.greeting_pe.GreetingPe

@Suppress("unused")
class LandingWebPage(userId: String = ""): IWebPage {
    init {
        "LandingPage created with userId: $userId".println()
    }

    override fun getResponse(): String {
        return BasicWebPage()
            .setTitle("Welcome")
            .addPageElement(GreetingPe())
            .getResponse() as String
    }
}

fun main() {
    BasicWebPage()
        .addPageElement(GreetingPe())
        .getResponse()
        .println()
}