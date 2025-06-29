package io.schinzel.sample.pages.page_with_block

import io.schinzel.web_blocks.component.page.PageBuilder
import io.schinzel.web_blocks.web.routes.IPageRoute
import io.schinzel.sample.pages.page_with_block.greeting_pe.GreetingBlock

@Suppress("unused")
class ThePage(userId: String = "") : IPageRoute {

    override suspend fun getResponse(): String {
        return PageBuilder()
            .setTitle("Welcome")
            .addRow()
            // The columnSpan determines how wide the column is
            .addColumn(columnSpan = 12)
            .addBlock(GreetingBlock())
            .getHtml()
    }
}
