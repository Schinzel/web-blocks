package io.schinzel.page_elements.samples.component.pages.landing.greeting_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor2

class GreetingPe : ObservablePageElement() {
    override fun getResponse(): String {
        return TemplateProcessor2(this)
            .addData("firstName", "Pelle")
            .processTemplate("GreetingPe.html")
    }

}

fun main() {
    GreetingPe().getHtml()
}