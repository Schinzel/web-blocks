package io.schinzel.sample.pages.page_with_page_elements_and_page_api_route

import io.schinzel.page_elements.component.page.PageBuilder
import io.schinzel.page_elements.web.routes.IPageRoute
import io.schinzel.sample.pages.page_with_page_elements_and_page_api_route.page_elements.intro_text.IntroductionTextPe
import io.schinzel.sample.pages.page_with_page_elements_and_page_api_route.page_elements.update_name_pe.UpdateNamePe
import io.schinzel.sample.pages.page_with_page_elements_and_page_api_route.page_elements.welcome_pe.WelcomePe

/**
 * The purpose of this page is to welcome a user and let the
 * user update its' name
 *
 */
@Suppress("unused")
class Page(private val userId: Int) : IPageRoute {
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

        val response = PageBuilder()
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
