package io.schinzel.sample.pages.landing.greeting_pe

import io.schinzel.pages.IPageElement
import io.schinzel.web_app_engine.template_engine.TemplateRenderer

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