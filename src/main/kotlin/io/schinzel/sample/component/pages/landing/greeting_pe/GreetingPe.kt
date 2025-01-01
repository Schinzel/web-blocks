package io.schinzel.sample.component.pages.landing.greeting_pe

import io.schinzel.pages.bootstrap_page.ObservablePageElement
import io.schinzel.pages.template_engine.TemplateRenderer

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