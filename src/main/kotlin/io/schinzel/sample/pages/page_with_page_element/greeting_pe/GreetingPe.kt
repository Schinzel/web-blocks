package io.schinzel.sample.pages.page_with_page_element.greeting_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.page_elements.component.template_engine.TemplateProcessor

class GreetingPe : ObservablePageElement() {
    override suspend fun getResponse(): String {
        return TemplateProcessor(this)
            // Set that variable firstName is Pelle
            .addData("firstName", "Pelle")
            // Read the file template file and return HTML
            .processTemplate("GreetingPe.html")
    }

}

// Removed main function that calls getHtml() synchronously