package io.schinzel.sample.pages.page_with_block

import io.schinzel.sample.pages.page_with_block.greeting_block.GreetingBlock
import io.schinzel.web_blocks.component.page_builder.PageBuilder
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

@Page
@Suppress("unused")
class ThePage(
    userId: String = "",
) : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse =
        html(
            PageBuilder()
                .setTitle("Welcome")
                .addRow()
                // The columnSpan determines how wide the column is
                .addColumn(columnSpan = 12)
                .addBlock(GreetingBlock())
                .getHtml(),
        )
}
