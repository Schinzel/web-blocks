package io.schinzel.page_elements_kotlin.pages.landing.greeting_pe

import io.schinzel.page_elements_kotlin.stuff.IPageElement
import io.schinzel.page_elements_kotlin.stuff.TemplateProcessor

class GreetingPe : IPageElement {
    override fun getHtml(): String {
        return TemplateProcessor("GreetingPe.html", this)
            .addData("firstName", "Pelle")
            .getProcessedTemplate()
    }

}

fun main() {
    GreetingPe().getHtml()
}