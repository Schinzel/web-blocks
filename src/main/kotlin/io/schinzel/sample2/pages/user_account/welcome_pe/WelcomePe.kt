package io.schinzel.sample2.pages.user_account.welcome_pe

import io.schinzel.sample2.pages.user_account.name_pe.IPageElement
import io.schinzel.sample2.pages.user_account.name_pe.NameReadDao

class WelcomePe(private val userId: Int) : IPageElement {
    private val firstName = NameReadDao(userId).getFirstName()
    override fun getHtml(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}