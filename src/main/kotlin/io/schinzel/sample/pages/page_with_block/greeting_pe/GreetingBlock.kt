package io.schinzel.sample.pages.page_with_block.greeting_pe

import io.schinzel.web_blocks.component.page.ObservableBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor

class GreetingBlock : ObservableBlock() {
    override suspend fun getResponse(): String {
        return TemplateProcessor(this)
            // Set that variable firstName is Pelle
            .addData("firstName", "Pelle")
            // Read the file template file and return HTML
            .processTemplate("GreetingBlock.html")
    }

}

// Removed main function that calls getHtml() synchronously