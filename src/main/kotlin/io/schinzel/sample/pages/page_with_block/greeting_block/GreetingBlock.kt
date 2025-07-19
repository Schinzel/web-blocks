package io.schinzel.sample.pages.page_with_block.greeting_block

import io.schinzel.web_blocks.component.page.WebBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPageApi

@WebBlockPageApi
class GreetingBlock : WebBlock() {
    override suspend fun getResponse(): WebBlockResponse =
        html(
            TemplateProcessor(this)
                // Set that variable firstName is Pelle
                .addData("firstName", "Pelle")
                // Read the file template file and return HTML
                .processTemplate("GreetingBlock.html"),
        )
}

// Removed main function that calls getHtml() synchronously
