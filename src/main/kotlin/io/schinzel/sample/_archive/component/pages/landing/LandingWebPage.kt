package io.schinzel.sample._archive.component.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.page_elements.component.page.Page
import io.schinzel.page_elements.web.routes.IPageRoute
import io.schinzel.sample._archive.component.pages.landing.greeting_pe.GreetingPe

@Suppress("unused")
class LandingWebPage(userId: String = "") : IPageRoute {
    init {
        "LandingPage created with userId: $userId".println()
    }

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
