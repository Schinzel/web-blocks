package io.schinzel.sample.pages.page_with_blocks_and_page_api_route

import io.schinzel.web_blocks.component.page.PageBuilder
import io.schinzel.web_blocks.web.routes.IPageRoute
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text.IntroductionTextBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_pe.UpdateNameBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_pe.WelcomeBlock

/**
 * The purpose of this page is to welcome a user and let the
 * user update its' name
 *
 */
@Suppress("unused")
class WelcomePage(private val userId: Int) : IPageRoute {
    override suspend fun getResponse(): String {
        // Create blocks
        val welcomeBlock = WelcomeBlock(userId)
        val updateNameBlock = UpdateNameBlock(userId)
        val introTextBlock = IntroductionTextBlock(userId)

        // Set up observers
        updateNameBlock
            // welcome-block will observe changes
            // in update-name-block
            .addObserver(welcomeBlock)
            // intro-text-block will observe changes
            // in update-name-block
            .addObserver(introTextBlock)

        val pageBuilder = PageBuilder()
            .setTitle("Account")

            // Add welcome message block
            .addRow()
            .addColumn(12)
            .addBlock(welcomeBlock)

            // Add block for displaying and updating the name
            .addRow()
            .addColumn(12)
            .addBlock(updateNameBlock)

            // Add block for introduction text
            .addRow()
            .addColumn(12)
            .addBlock(introTextBlock)

        return pageBuilder.getHtml()
    }
}
