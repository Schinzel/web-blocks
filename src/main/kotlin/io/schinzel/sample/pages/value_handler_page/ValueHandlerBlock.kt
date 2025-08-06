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

typealias FirstName = String
typealias UserId = String

@PageBlock
class ValueHandlerBlock : WebBlock() {
    private val userId = 123
    private val firstName = "John Doe"

    companion object {
        private const val VALUE_HANDLER_ID = "value_handler_id_123"

        init {


            val valueHandler = object : IValueHandler<FirstName, UserId> {
                override suspend fun handle(value: FirstName, context: UserId): HtmlContentResponse {
                    "Got the value of $value with the context user-id $context".println()
                    return html("<h1>First name: $value. User id: $context</h1>")
                }
            }
            ValueHandlerRegistry.instance.register(VALUE_HANDLER_ID, valueHandler)

        }
    }

    override suspend fun getResponse(): IHtmlResponse {
        val html = TemplateProcessor(this)
            .withData("firstName", firstName)
            .withData("userId", userId)
            .withData("valueHandlerId", VALUE_HANDLER_ID)
            .processTemplate("value_handler_block.html")
        return html(html)
    }
}
