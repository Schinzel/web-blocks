package io.schinzel.samples.new_sample.pages.page_with_page_endpoint

import io.schinzel.page_elements.component.page.Page
import io.schinzel.page_elements.web.response_handlers.IPageResponseHandler
import io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.intro_text.IntroductionTextPe
import io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.update_name_pe.UpdateNamePe
import io.schinzel.samples.new_sample.pages.page_with_page_endpoint.page_elements.welcome_pe.WelcomePe

/**
 * The purpose of this page is to welcome a user and let the
 * user update its' name
 *
 */
@Suppress("unused")
class AccountWebPage(private val userId: Int) : IPageResponseHandler {
    override fun getResponse(): String {
        // Create page elements
        val welcomePe = WelcomePe(userId)
        val updateNamePe = UpdateNamePe(userId)
        val introTextPe = IntroductionTextPe(userId)

        // Set up observers
        updateNamePe
            // welcome-page-element will observe changes
            // in update-name-page-element
            .addObserver(welcomePe)
            // intro-text-page-element will observe changes
            // in update-name-page-element
            .addObserver(introTextPe)

        val response = Page()
            .setTitle("Account")

            // Add welcome message page element
            .addRow()
            .addColumn(12)
            .addPageElement(welcomePe)

            // Add page element for displaying and updating the name
            .addRow()
            .addColumn(12)
            .addPageElement(updateNamePe)

            // Add page element for introduction text
            .addRow()
            .addColumn(12)
            .addPageElement(introTextPe)

            .getHtml()
        return response
    }
}
