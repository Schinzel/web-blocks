package io.schinzel.page_elements.samples.component.pages.landing.greeting_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateRenderer

class GreetingPe : ObservablePageElement() {
    override fun getResponse(): String {
        return TemplateRenderer("GreetingPe.html", this)
            .addData("firstName", "Pelle")
            .process()
    }

}

fun main() {
    GreetingPe().getHtml()
}