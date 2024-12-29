package io.schinzel.sample.pages.user_account

import io.schinzel.pages.bootstrap_page.BootstrapPage
import io.schinzel.sample.pages.user_account.intro_text.IntroductionTextPe
import io.schinzel.sample.pages.user_account.update_name_pe.UpdateNamePe
import io.schinzel.sample.pages.user_account.welcome_pe.WelcomePe
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class AccountWebPage(private val userId: Int) : IPageResponseHandler {
    override fun getResponse(): String {
        val welcomePe = WelcomePe(userId)
        val updateNamePe = UpdateNamePe(userId)
        val introTextPe = IntroductionTextPe(userId)
        updateNamePe
            .addObserver(welcomePe)
            .addObserver(introTextPe)

        val response = BootstrapPage()
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
