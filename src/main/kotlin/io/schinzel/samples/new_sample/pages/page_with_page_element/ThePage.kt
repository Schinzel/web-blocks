package io.schinzel.samples.new_sample.pages.page_with_page_element

import io.schinzel.page_elements.component.page.Page
import io.schinzel.page_elements.web.response_handlers.IPageResponseHandler
import io.schinzel.samples.new_sample.pages.page_with_page_element.greeting_pe.GreetingPe

@Suppress("unused")
class ThePage(userId: String = "") : IPageResponseHandler {

    override fun getResponse(): String {
        return Page()
            .setTitle("Welcome")
            .addRow()
            // The columnSpan determines how wide the column is
            .addColumn(columnSpan = 12)
            .addPageElement(GreetingPe())
            .getHtml()
    }
}
