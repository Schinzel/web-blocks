package io.schinzel.samples.component.pages.user_account

import io.schinzel.component.page.Page
import io.schinzel.samples.component.pages.user_account.intro_text.IntroductionTextPe
import io.schinzel.samples.component.pages.user_account.update_name_pe.UpdateNamePe
import io.schinzel.samples.component.pages.user_account.welcome_pe.WelcomePe
import io.schinzel.web.response_handlers.IPageResponseHandler

@Suppress("unused")
class AccountWebPage(private val userId: Int) : IPageResponseHandler {
    override fun getResponse(): String {
        val welcomePe = WelcomePe(userId)
        val updateNamePe = UpdateNamePe(userId)
        val introTextPe = IntroductionTextPe(userId)
        updateNamePe
            .addObserver(welcomePe)
            .addObserver(introTextPe)

        val response = Page()
            .setTitle("Account")

            .addRow()
            .addColumn(12)
            .addPageElement(welcomePe)

            .addRow()
            .addColumn(12)
            .addPageElement(updateNamePe)

            .addRow()
            .addColumn(12)
            .addPageElement(introTextPe)

            .getHtml()
        return response
    }
}
