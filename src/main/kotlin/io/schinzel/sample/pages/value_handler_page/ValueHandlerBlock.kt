package io.schinzel.sample.pages.value_handler_page

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.web_blocks.component.page_builder.WebBlock
import io.schinzel.web_blocks.component.template_engine.TemplateProcessor
import io.schinzel.web_blocks.component.value_handler.IValueHandler
import io.schinzel.web_blocks.component.value_handler.ValueHandlerRegistry
import io.schinzel.web_blocks.web.response.HtmlContentResponse
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.annotations.PageBlock

@PageBlock
class ValueHandlerBlock : WebBlock() {
    private val userId = 123
    private val firstName = "John Doe" // Simplified for now

    init {
        val valueHandler = object : IValueHandler<String> {
            override suspend fun handle(value: String): HtmlContentResponse {
                "Got the value of $firstName".println()
                return html("<h1>$firstName</h1>")
            }
        }
        ValueHandlerRegistry.instance
            .register("value_handler_id_123", valueHandler)
    }

    override suspend fun getResponse(): IHtmlResponse {
        val html = TemplateProcessor(this)
            .withData("firstName", firstName)
            .withData("userId", userId)
            .processTemplate("value_handler_block_template.html")
        return html(html)
    }
}
