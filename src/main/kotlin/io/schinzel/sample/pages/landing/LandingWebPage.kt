package io.schinzel.sample.pages.landing

import io.schinzel.basic_utils_kotlin.println
import io.schinzel.pages.bootstrap_page.BootstrapPage
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler
import io.schinzel.sample.pages.landing.greeting_pe.GreetingPe

@Suppress("unused")
class LandingWebPage(userId: String = ""): IPageResponseHandler {
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
