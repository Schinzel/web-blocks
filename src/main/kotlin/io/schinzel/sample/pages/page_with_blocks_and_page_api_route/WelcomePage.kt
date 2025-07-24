package io.schinzel.sample.pages.page_with_blocks_and_page_api_route

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text.IntroductionTextBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block.UpdateNamePageBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block.WelcomeBlock
import io.schinzel.web_blocks.component.page_builder.PageBuilder
import io.schinzel.web_blocks.web.response.IHtmlResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IHtmlRoute
import io.schinzel.web_blocks.web.routes.annotations.Page

/**
 * The purpose of this page is to welcome a user and let the
 * user update its' name
 *
 */
@Page
@Suppress("unused")
class WelcomePage(
    private val userId: Int,
) : IHtmlRoute {
    override suspend fun getResponse(): IHtmlResponse {
        // Create blocks
        val welcomeBlock = WelcomeBlock(userId)
        val updateNameBlock = UpdateNamePageBlock(userId)
        val introTextBlock = IntroductionTextBlock(userId)

        // Set up observers
        updateNameBlock
            // welcome-block will observe changes
            // in update-name-block
            .addObserver(welcomeBlock)
            // intro-text-block will observe changes
            // in update-name-block
            .addObserver(introTextBlock)

        val pageBuilder =
            PageBuilder()
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

        return html(pageBuilder.getHtml())
    }
}
