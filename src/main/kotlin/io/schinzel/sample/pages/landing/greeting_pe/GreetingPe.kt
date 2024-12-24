package io.schinzel.sample.pages.landing.greeting_pe

import io.schinzel.page_elements.web_response.IPageElement
import io.schinzel.page_elements.template_engine.TemplateRenderer

class GreetingPe : IPageElement {
    override fun getHtml(): String {
        return TemplateRenderer("GreetingPe.html", this)
            .addData("firstName", "Pelle")
            .process()
    }

}

fun main() {
    GreetingPe().getHtml()
}