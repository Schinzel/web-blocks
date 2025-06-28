package io.schinzel._old_samples.component.pages.user_account

import io.schinzel._old_samples.component.pages.user_account.intro_text.IntroductionTextPe
import io.schinzel._old_samples.component.pages.user_account.update_name_pe.UpdateNamePe
import io.schinzel._old_samples.component.pages.user_account.welcome_pe.WelcomePe
import io.schinzel.page_elements.component.page.PageBuilder
import io.schinzel.page_elements.web.routes.IPageRoute

@Suppress("unused")
class AccountWebPage(private val userId: Int) : IPageRoute {
    override fun getResponse(): String {
        val welcomePe = WelcomePe(userId)
        val updateNamePe = UpdateNamePe(userId)
        val introTextPe = IntroductionTextPe(userId)
        updateNamePe
            .addObserver(welcomePe)
            .addObserver(introTextPe)

        val response = PageBuilder()
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
