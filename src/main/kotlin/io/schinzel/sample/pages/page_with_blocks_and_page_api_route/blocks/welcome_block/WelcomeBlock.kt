package io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.welcome_block

import io.schinzel.web_blocks.component.page.ObservableBlock
import io.schinzel.sample.pages.page_with_blocks_and_page_api_route.blocks.NameDao

class WelcomeBlock(val userId: Int) : ObservableBlock() {

    private val firstName = NameDao(userId).getFirstName()
    override suspend fun getResponse(): String {
        return "<h1>Welcome $firstName</h1>"
    }
}