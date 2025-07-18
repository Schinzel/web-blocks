package io.schinzel.sample.pages.page_with_block

import io.schinzel.sample.pages.page_with_block.greeting_block.GreetingBlock
import io.schinzel.web_blocks.component.page.PageBuilder
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

@WebBlockPage
@Suppress("unused")
class ThePage(
    userId: String = "",
) : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse =
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
