package io.schinzel.sample.pages.landing.greeting_pe

import io.schinzel.page_elements.IPageElement
import io.schinzel.page_elements.TemplateProcessor

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