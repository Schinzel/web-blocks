package io.schinzel.page_elements_kotlin.page.greeting_pe

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements_kotlin.IPageElement
import io.schinzel.stuff.ITemplateProcessorDeep
import io.schinzel.stuff.TemplateProcessor

class GreetingPe : IPageElement, ITemplateProcessorDeep {
    override val data = mutableMapOf<String, String>()
    override fun getHtml(): String {

        return TemplateProcessor("GreetingPe.html")
            .addData("firstName", "Pelle")
            .getProcessedTemplate()
    }

}

fun main() {
    GreetingPe().getHtml().println()
}