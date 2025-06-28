package io.schinzel._old_samples.component.pages.user_account.welcome_pe

import io.schinzel._old_samples.component.pages.user_account.NameDao
import io.schinzel.page_elements.component.page.ObservablePageElement

class WelcomePe(val userId: Int) : ObservablePageElement() {

    private val firstName = NameDao(userId).getFirstName()
    override fun getResponse(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}