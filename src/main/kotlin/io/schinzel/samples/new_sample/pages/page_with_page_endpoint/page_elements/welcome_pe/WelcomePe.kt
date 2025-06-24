package io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.welcome_pe

import io.schinzel.page_elements.component.page.ObservablePageElement
import io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.NameDao

class WelcomePe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}