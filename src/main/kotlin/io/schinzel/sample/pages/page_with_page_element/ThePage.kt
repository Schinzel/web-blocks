package io.schinzel.sample.pages.page_with_page_element

import io.schinzel.page_elements.component.page.PageBuilder
import io.schinzel.page_elements.web.routes.IPageRoute
import io.schinzel.sample.pages.page_with_page_element.greeting_pe.GreetingPe

@Suppress("unused")
class ThePage(userId: String = "") : IPageRoute {

    override suspend fun getResponse(): String {
        return PageBuilder()
            .setTitle("Welcome")
            .addRow()
            // The columnSpan determines how wide the column is
            .addColumn(columnSpan = 12)
            .addPageElement(GreetingPe())
            .getHtml()
    }
}
