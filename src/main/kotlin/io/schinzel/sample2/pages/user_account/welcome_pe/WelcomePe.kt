package io.schinzel.sample2.pages.user_account.welcome_pe

import io.schinzel.basicutils.RandomUtil
import io.schinzel.pages.bootstrap_page_v2.IObserverAndSubject
import io.schinzel.pages.bootstrap_page_v2.ObservablePageElement
import io.schinzel.sample2.pages.user_account.NameDao

class WelcomePe(val userId: Int) : ObservablePageElement {
    override val guid: String = RandomUtil.getRandomString(10)
    override val observers = mutableListOf<IObserverAndSubject>()

    private val firstName = NameDao(userId).getFirstName()
    override fun getHtml(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}