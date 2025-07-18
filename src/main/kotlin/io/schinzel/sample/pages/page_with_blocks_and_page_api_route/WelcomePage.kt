package io.schinzel.sample.pages.page_with_blocks_and_page_api_route

import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.intro_text.IntroductionTextBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.update_name_block.UpdateNameBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block.WelcomeBlock
import io.schinzel.web_blocks.component.page.PageBuilder
import io.schinzel.web_blocks.web.response.WebBlockResponse
import io.schinzel.web_blocks.web.response.html
import io.schinzel.web_blocks.web.routes.IWebBlockRoute
import io.schinzel.web_blocks.web.routes.annotations.WebBlockPage

/**
 * The purpose of this page is to welcome a user and let the
 * user update its' name
 *
 */
@WebBlockPage
@Suppress("unused")
class WelcomePage(
    private val userId: Int,
) : IWebBlockRoute {
    override suspend fun getResponse(): WebBlockResponse {
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
