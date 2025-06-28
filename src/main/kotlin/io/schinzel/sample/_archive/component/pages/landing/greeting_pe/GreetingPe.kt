package io.schinzel.sample._archive.component.pages.landing.greeting_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor

class GreetingPe : ObservablePageElement() {
    override fun getResponse(): String {
        return TemplateProcessor(this)
            // Set that variable firstName is Pelle
            .addData("firstName", "Pelle")
            // Read the file template file and return HTML
            .processTemplate("GreetingPe.html")
    }

}

fun main() {
    GreetingPe().getHtml()
}