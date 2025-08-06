package io.schinzel.sample.pages.value_handler_page

import io.schinzel.web_blocks.component.page_builder.PageBuilder
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Page
@Suppress("unused")
class Page : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse {
        val html =
            PageBuilder()
                .setTitle("Value Handle Sample Page")
                .addRow()
                .addColumn(12)
                .addBlock(ValueHandlerBlock())
                .getHtml()
        return html(html)
    }
}
