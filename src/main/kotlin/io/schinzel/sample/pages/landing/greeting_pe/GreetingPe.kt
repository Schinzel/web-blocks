package io.schinzel.sample.pages.landing.greeting_pe

import io.schinzel.sample2.pages.user_account.name_pe.IPageElement
import io.schinzel.pages.template_engine.TemplateRenderer

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