package io.schinzel.sample2.pages.user_account

import io.schinzel.pages.bootstrap_page_v2.BootstrapPageV2
import io.schinzel.sample2.pages.user_account.update_name_pe.UpdateNamePe
import io.schinzel.sample2.pages.user_account.welcome_pe.WelcomePe
import io.schinzel.web_app_engine.response_handlers.response_handlers.IPageResponseHandler

@Suppress("unused")
class AccountWebPage(private val userId: Int) : IPageResponseHandler {
    override fun getResponse(): String {
        val welcomePe = WelcomePe(userId)
        val updateNamePe = UpdateNamePe(userId)
        updateNamePe.addObserver(welcomePe)

        val response = BootstrapPageV2()
            .setTitle("Account")

            .addRow()
            .addColumn(12)
            .addPageElement(welcomePe)

            .addRow()
            .addColumn(12)
            .addPageElement(updateNamePe)

            .getHtml()
        return response
    }
}
