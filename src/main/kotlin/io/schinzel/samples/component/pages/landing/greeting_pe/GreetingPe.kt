package io.schinzel.samples.component.pages.landing.greeting_pe

import io.schinzel.components.bootstrap_page.ObservablePageElement
import io.schinzel.components.template_engine.TemplateRenderer

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