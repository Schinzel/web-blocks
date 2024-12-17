package io.schinzel.page_elements_kotlin.landing_page.greeting_pe

import io.schinzel.stuff.IPageElement
import io.schinzel.stuff.TemplateProcessor

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