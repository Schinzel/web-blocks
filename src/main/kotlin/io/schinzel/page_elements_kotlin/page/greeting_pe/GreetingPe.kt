package io.schinzel.page_elements_kotlin.page.greeting_pe

import io.schinzel.page_elements_kotlin.IPageElement
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