package io.schinzel.page_elements_kotlin.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.stuff.basic_page.BasicPage
import io.schinzel.page_elements_kotlin.landing.greeting_pe.GreetingPe

class LandingPage {
    fun getHtml(): String {
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