package io.schinzel.sample.pages.page_with_block.greeting_block

import io.schinzel.web_blocks.component.page_builder.WebBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.PageBlock

@PageBlock
class GreetingBlock : WebBlock() {
    override suspend fun getResponse(): IHtmlResponse =
        html(
            TemplateProcessor(this)
                // Set that variable firstName is Pelle
                .addData("firstName", "Pelle")
                // Read the file template file and return HTML
                .processTemplate("GreetingBlock.html"),
        )
}
