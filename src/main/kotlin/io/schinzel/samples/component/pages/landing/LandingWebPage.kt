package io.schinzel.samples.component.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.components.bootstrap_page.BootstrapPage
import io.schinzel.samples.component.pages.landing.greeting_pe.GreetingPe
import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class LandingWebPage(userId: String = "") : IPageResponseHandler {
    init {
        "LandingPage created with userId: $userId".println()
    }

    override fun getResponse(): String {
        return BootstrapPage()
            .setTitle("Welcome")
            .addRow()
            .addColumn(12)
            .addPageElement(GreetingPe())
            .getHtml()
    }
}
